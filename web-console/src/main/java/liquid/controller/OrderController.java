package liquid.controller;

import liquid.context.BusinessContext;
import liquid.domain.Order;
import liquid.domain.ServiceItem;
import liquid.facade.OrderFacade;
import liquid.metadata.*;
import liquid.persistence.domain.*;
import liquid.service.*;
import liquid.utils.RoleHelper;
import liquid.validation.FormValidationResult;
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
import java.security.Principal;
import java.util.Date;
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
public class OrderController extends BaseChargeController {
    private static final Logger logger = LoggerFactory.getLogger(OrderEntity.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CargoTypeService cargoTypeService;

    @Autowired
    private PlanningService planningService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private BusinessContext businessContext;

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

    @ModelAttribute("serviceTypes")
    public Iterable<ServiceTypeEntity> populateServiceTypes() {
        return serviceTypeService.findAll();
    }

    @ModelAttribute("customers")
    public Iterable<CustomerEntity> populateCustomers() {
        return customerService.findAll();
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

    @ModelAttribute("chargeTypeMap")
    public Map<Long, String> populateChargeTypes() {return chargeService.getChargeTypes(); }

    @ModelAttribute("serviceSubtypes")
    public Iterable<ServiceSubtypeEntity> populateServiceSubtyes() {return serviceSubtypeService.findEnabled(); }

    @ModelAttribute("locations")
    public List<LocationEntity> populateLocations() {
        return locationService.findByType(LocationType.CITY.getType());
    }

    @Deprecated
    @RequestMapping(method = RequestMethod.GET)
    public String initFind(Model model, Principal principal) {
        model.addAttribute("orders", orderService.findAllOrderByDesc());
        return "order/find";
    }

    @RequestMapping(method = RequestMethod.GET, params = "number")
    public String initFindPaging(@RequestParam int number,
                                 Model model, Principal principal) {
        int size = 20;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        String role = RoleHelper.getRole(principal);
        Page<OrderEntity> page = orderService.findByUpdateUser(principal.getName(), pageRequest);
        model.addAttribute("page", page);
        return "order/page";
    }

    @RequestMapping(method = RequestMethod.GET, params = "findByOrderNo")
    public String findById(@RequestParam String param, Model model) {
        logger.debug("param: {}", param);

        model.addAttribute("orders", orderService.findByOrderNo(param));
        return "order/find";
    }

    @RequestMapping(method = RequestMethod.GET, params = "findByCustomerName")
    public String findByCustomerName(@RequestParam String param, Model model) {
        logger.debug("param: {}", param);

        model.addAttribute("orders", orderService.findByCustomerName(param));
        return "order/find";
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
    public String save(@Valid @ModelAttribute Order order,
                       BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("order: {}", order);
        FormValidationResult result = customerService.validateCustomer(order);
        if (!result.isSuccessful()) {
            setFieldError(bindingResult, "order", "customerName", order.getCustomerName());
        }
        if (bindingResult.hasErrors()) {
            List<LocationEntity> locationEntities = locationService.findByType(LocationType.CITY.getType());
            model.addAttribute("locations", locationEntities);
            return "order/form";
        } else {
            order.setRole(RoleHelper.getRole(principal));
            order.setUsername(principal.getName());
            order.setStatus(OrderStatus.SAVED.getValue());
            orderFacade.save(order);
            return "redirect:/order?number=0";
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "submit")
    public String submit(@Valid @ModelAttribute Order order,
                         BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("order: {}", order);
        FormValidationResult result = customerService.validateCustomer(order);
        if (!result.isSuccessful()) {
            setFieldError(bindingResult, "order", "customerName", order.getCustomerName());
        }
        if (bindingResult.hasErrors()) {
            List<LocationEntity> locationEntities = locationService.findByType(LocationType.CITY.getType());
            model.addAttribute("locations", locationEntities);
            return "order/form";
        } else {
            order.setRole(RoleHelper.getRole(principal));
            order.setUsername(principal.getName());
            order.setStatus(OrderStatus.SUBMITTED.getValue());
            orderFacade.submit(order);
            return "redirect:/order?number=0";
        }
    }

    public String submit(@Valid @ModelAttribute OrderEntity order,
                         BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("order: {}", order);

        // TODO: add to interceptor.
        businessContext.setUsername(principal.getName());

        order.setStatus(OrderStatus.SUBMITTED.getValue());

        FormValidationResult result = customerService.validateCustomer(order);
        if (!result.isSuccessful()) {
            setFieldError(bindingResult, "order", "customerName0", order.getCustomerName0());
        }

        if (bindingResult.hasErrors()) {
            List<LocationEntity> locationEntities = locationService.findByType(LocationType.CITY.getType());
            model.addAttribute("locations", locationEntities);
            return "order/form";
        } else {
            order.setCreateRole(RoleHelper.getRole(principal));
            order.setCreateUser(principal.getName());
            order.setCreateTime(new Date());
            order.setUpdateUser(principal.getName());
            order.setUpdateTime(new Date());
            orderService.submit(order);
            return "redirect:/order?number=0";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable long id,
                         Model model, Principal principal) {
        logger.debug("id: {}", id);

        OrderEntity order = orderService.find(id);
        List<LocationEntity> locationEntities = locationService.findByType(LocationType.CITY.getType());
        model.addAttribute("locations", locationEntities);
        model.addAttribute("order", order);
        model.addAttribute("tab", "detail");

        Planning planning = planningService.findByOrder(order);
        routeService.findByPlanning(planning);

        model.addAttribute("transModes", TransMode.toMap());
        model.addAttribute("planning", planning);

        return "order/detail";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String getOrder(@PathVariable long id,
                           Model model, Principal principal) {
        logger.debug("id: {}", id);
        Order order = orderFacade.find(id);
        logger.debug("order: {}", order);

        model.addAttribute("order", order);
        return "order/form";
    }

    @RequestMapping(value = "/{id}/duplicate", method = RequestMethod.GET)
    public String initDuplicate(@PathVariable long id,
                                Model model, Principal principal) {
        logger.debug("id: {}", id);

        OrderEntity order = orderService.find(id);
        logger.debug("order: {}", order);

        List<LocationEntity> locationEntities = locationService.findByType(LocationType.CITY.getType());

        order = orderService.duplicate(order);
        logger.debug("order: {}", order);

        model.addAttribute("locations", locationEntities);
        model.addAttribute("order", order);
        return "order/form";
    }

    @RequestMapping(value = "/{id}/{tab}", method = RequestMethod.GET)
    public String charge(@PathVariable long id,
                         @PathVariable String tab,
                         Model model, Principal principal) {
        logger.debug("id: {}", id);
        logger.debug("tab: {}", tab);

        OrderEntity order = orderService.find(id);

        switch (tab) {
            case "task":
                List<Task> tasks = taskService.listTasksByOrderId(id);
                List<HistoricTaskInstance> completedTasks = taskService.listCompltedTasks(id);
                model.addAttribute("tasks", tasks);
                model.addAttribute("completedTasks", completedTasks);
                break;
            case "charge":
                Iterable<Charge> charges = chargeService.getChargesByOrderId(id);

                model.addAttribute("cts", chargeTypesToMap());
                model.addAttribute("chargeWays", ChargeWay.values());
                model.addAttribute("charges", charges);
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
