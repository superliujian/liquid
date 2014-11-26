package liquid.controller;

import liquid.accounting.persistence.domain.ChargeEntity;
import liquid.container.domain.ContainerType;
import liquid.container.persistence.domain.ContainerSubtypeEntity;
import liquid.container.service.ContainerSubtypeService;
import liquid.domain.LoadingType;
import liquid.domain.LocationType;
import liquid.domain.ServiceItem;
import liquid.domain.TradeType;
import liquid.metadata.ChargeWay;
import liquid.metadata.ContainerCap;
import liquid.metadata.OrderStatus;
import liquid.order.domain.Order;
import liquid.order.facade.OrderFacade;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.persistence.domain.*;
import liquid.security.SecurityContext;
import liquid.service.*;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.service.ShipmentService;
import liquid.validation.FormValidationResult;
import liquid.web.domain.SearchBarForm;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 2:38 PM
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CargoTypeService cargoTypeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private RailwayPlanTypeService railwayPlanTypeService;

    @ModelAttribute("serviceTypes")
    public Iterable<ServiceTypeEntity> populateServiceTypes() {
        return serviceTypeService.findAll();
    }

    @ModelAttribute("cargos")
    public Iterable<GoodsEntity> populateCargos() {
        return cargoTypeService.findAll();
    }

    @ModelAttribute("tradeTypes")
    public TradeType[] populateTradeTypes() {
        return TradeType.values();
    }

    @ModelAttribute("loadingTypes")
    public LoadingType[] populateLoadings() {
        return LoadingType.values();
    }

    @ModelAttribute("containerTypeMap")
    public Map<Integer, String> populateContainerTypes() {
        return ContainerType.toMap();
    }

    @Deprecated
    @ModelAttribute("containerCaps")
    public ContainerCap[] populateContainerCaps() {
        return ContainerCap.values();
    }

    @ModelAttribute("railContainerSubtypes")
    public Iterable<ContainerSubtypeEntity> populateRailContainerSubtypes() {
        return containerSubtypeService.findByContainerType(ContainerType.RAIL);
    }

    @ModelAttribute("selfContainerSubtypes")
    public Iterable<ContainerSubtypeEntity> populateOwnContainerSubtypes() {
        return containerSubtypeService.findByContainerType(ContainerType.SELF);
    }

    @ModelAttribute("status")
    public OrderStatus[] populateStatus() {
        return OrderStatus.values();
    }

    @ModelAttribute("serviceSubtypes")
    public Iterable<ServiceSubtypeEntity> populateServiceSubtyes() {return serviceSubtypeService.findEnabled(); }

    @ModelAttribute("locations")
    public List<LocationEntity> populateLocations() {
        return locationService.findByType(LocationType.CITY.getType());
    }

    @ModelAttribute("railwayPlanTypes")
    public Iterable<RailPlanTypeEntity> populateRailwayPlanTypes() {
        return railwayPlanTypeService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initFindPaging(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        String username = SecurityContext.getInstance().getUsername();

        Page<OrderEntity> page;

        switch (SecurityContext.getInstance().getRole()) {
            case "ROLE_SALES":
            case "ROLE_MARKETING":
                page = orderService.findByCreateUser(username, pageRequest);
                break;
            default:
                page = orderService.findAll(pageRequest);
                break;
        }

        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/order?");

        SearchBarForm searchBarForm = new SearchBarForm();
        searchBarForm.setAction("/order");
        searchBarForm.setTypes(new String[][]{{"orderNo", "order.no"}, {"customerName", "customer.name"}});
        model.addAttribute("searchBarForm", searchBarForm);
        return "order/page";
    }

    @RequestMapping(method = RequestMethod.GET, params = {"type", "text"})
    public String search(@RequestParam(defaultValue = "0", required = false) int number, SearchBarForm searchBarForm, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        String orderNo = "orderNo".equals(searchBarForm.getType()) ? searchBarForm.getText() : null;
        String customerName = "customerName".equals(searchBarForm.getType()) ? searchBarForm.getText() : null;
        String username = SecurityContext.getInstance().getUsername();

        Page<OrderEntity> page;

        switch (SecurityContext.getInstance().getRole()) {
            case "ROLE_SALES":
            case "ROLE_MARKETING":
                page = orderService.findAll(orderNo, customerName, username, pageRequest);
                break;
            default:
                page = orderService.findAll(orderNo, customerName, null, pageRequest);
                break;
        }

        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/order?type=" + searchBarForm.getType() + "&text=" + searchBarForm.getText() + "&");

        searchBarForm.setAction("/order");
        searchBarForm.setTypes(new String[][]{{"orderNo", "order.no"}, {"customerName", "customer.name"}});
        model.addAttribute("searchBarForm", searchBarForm);
        return "order/page";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String initCreationForm(Model model) {
        Order order = orderFacade.initOrder();

        model.addAttribute("order", order);
        return "order/form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "addServiceItem")
    public String addServiceItem(@ModelAttribute Order order) {
        logger.debug("order: {}", order);
        order.getServiceItems().add(new ServiceItem());

        return "order/form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "removeServiceItem")
    public String removeRow(@ModelAttribute Order order, HttpServletRequest request) {
        final Integer rowId = Integer.valueOf(request.getParameter("removeServiceItem"));
        order.getServiceItems().remove(rowId.intValue());
        return "order/form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public String save(@Valid @ModelAttribute Order order, BindingResult bindingResult, Model model) {
        logger.debug("order: {}", order);
        FormValidationResult result = orderFacade.validateCustomer(order);
        if (!result.isSuccessful()) {
            setFieldError(bindingResult, "order", "customerName", order.getCustomerName());
        }
        if (bindingResult.hasErrors()) {
            List<LocationEntity> locationEntities = locationService.findByType(LocationType.CITY.getType());

            model.addAttribute("locations", locationEntities);
            return "order/form";
        } else {
            order.setStatus(OrderStatus.SAVED.getValue());
            orderFacade.save(order);
            return "redirect:/order?number=0";
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "submit")
    public String submit(@Valid @ModelAttribute Order order,
                         BindingResult bindingResult, Model model) {
        logger.debug("order: {}", order);
        FormValidationResult result = orderFacade.validateCustomer(order);
        if (!result.isSuccessful()) {
            setFieldError(bindingResult, "order", "customerName", order.getCustomerName());
        }
        if (bindingResult.hasErrors()) {
            List<LocationEntity> locationEntities = locationService.findByType(LocationType.CITY.getType());

            model.addAttribute("locations", locationEntities);
            return "order/form";
        } else {
            order.setStatus(OrderStatus.SUBMITTED.getValue());
            orderFacade.submit(order);

            return "redirect:/order?number=0";
        }
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String initEdit(@PathVariable long id, Model model) {
        logger.debug("id: {}", id);

        Order order = orderFacade.find(id);
        logger.debug("order: {}", order);

        model.addAttribute("order", order);
        return "order/form";
    }

    @RequestMapping(value = "/{id}/duplicate", method = RequestMethod.GET)
    public String initDuplicate(@PathVariable long id, Model model) {
        logger.debug("id: {}", id);

        Order order = orderFacade.duplicate(id);
        logger.debug("order: {}", order);

        model.addAttribute("order", order);
        return "order/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        Order order = orderFacade.find(id);
        List<LocationEntity> locationEntities = locationService.findByType(LocationType.CITY.getType());
        model.addAttribute("locations", locationEntities);
        model.addAttribute("order", order);
        model.addAttribute("tab", "detail");
        return "order/detail";
    }

    @RequestMapping(value = "/{id}/{tab}", method = RequestMethod.GET)
    public String charge(@PathVariable long id, @PathVariable String tab, Model model) {
        logger.debug("id: {}", id);
        logger.debug("tab: {}", tab);

        Order order = orderFacade.find(id);

        switch (tab) {
            case "task":
                List<Task> tasks = taskService.listTasksByOrderId(id);
                List<HistoricTaskInstance> completedTasks = taskService.listCompltedTasks(id);
                model.addAttribute("tasks", tasks);
                model.addAttribute("completedTasks", completedTasks);
                break;
            case "railway":
                OrderEntity orderEntity = orderService.find(id);
                break;
            case "container":
                Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(id);
                model.addAttribute("shipmentSet", shipmentSet);
                break;
            case "charge":
                Iterable<ChargeEntity> charges = chargeService.getChargesByOrderId(id);

                Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
                model.addAttribute("serviceSubtypes", serviceSubtypes);
                model.addAttribute("chargeWays", ChargeWay.values());
                model.addAttribute("charges", charges);
                break;
            case "amount":
                tab = "amount";
                break;
            default:
                tab = "detail";
                break;
        }

        model.addAttribute("order", order);
        model.addAttribute("tab", tab);
        return "order/" + tab;
    }
}
