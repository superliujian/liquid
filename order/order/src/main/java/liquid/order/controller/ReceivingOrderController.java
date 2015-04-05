package liquid.order.controller;

import liquid.container.domain.ContainerCap;
import liquid.container.domain.ContainerType;
import liquid.container.persistence.domain.ContainerSubtypeEntity;
import liquid.container.service.ContainerSubtypeService;
import liquid.operation.domain.Customer;
import liquid.operation.domain.Goods;
import liquid.operation.domain.Location;
import liquid.operation.domain.LocationType;
import liquid.operation.service.CustomerService;
import liquid.operation.service.GoodsService;
import liquid.operation.service.LocationService;
import liquid.order.domain.OrderStatus;
import liquid.order.domain.ReceivingOrderEntity;
import liquid.order.facade.ValueAddedOrderFacade;
import liquid.order.model.TransportedContainer;
import liquid.order.model.ValueAddedOrder;
import liquid.order.service.ReceivingOrderServiceImpl;
import liquid.persistence.domain.ServiceTypeEntity;
import liquid.service.ServiceTypeServiceImpl;
import liquid.web.controller.BaseController;
import liquid.web.domain.SearchBarForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 4:33 PM
 */
@Controller
@RequestMapping("/recv_order")
public class ReceivingOrderController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ReceivingOrderController.class);

    @Autowired
    private ReceivingOrderServiceImpl recvOrderService;

    @Autowired
    private ValueAddedOrderFacade valueAddedOrderFacade;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ServiceTypeServiceImpl serviceTypeService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @ModelAttribute("serviceTypes")
    public Iterable<ServiceTypeEntity> populateServiceTypes() {
        return serviceTypeService.findAll();
    }

    @ModelAttribute("customers")
    public Iterable<Customer> populateCustomers() {
        return customerService.findAll();
    }

    @ModelAttribute("cargos")
    public Iterable<Goods> populateCargoTypes() {
        return goodsService.findAll();
    }

    @ModelAttribute("locations")
    public Iterable<Location> populateLocations() {
        return locationService.findByTypeId(LocationType.CITY);
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

    @RequestMapping(method = RequestMethod.GET)
    public String initFind(@RequestParam(defaultValue = "0", required = false) int number, SearchBarForm searchBarForm, Model model) {
        Page<ValueAddedOrder> page = new PageImpl<ValueAddedOrder>(new ArrayList<>());
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        if ("customer".equals(searchBarForm.getType())) {
            page = valueAddedOrderFacade.findByCustomerId(searchBarForm.getId(), pageRequest);
        } else if ("order".equals(searchBarForm.getType())) {
            ValueAddedOrder order = valueAddedOrderFacade.find(searchBarForm.getId());
            List<ValueAddedOrder> orders = new ArrayList<>();
            orders.add(order);
            page = new PageImpl<ValueAddedOrder>(orders);
        } else {
            page = valueAddedOrderFacade.findAll(pageRequest);
        }

        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/recv_order?");

        searchBarForm.setAction("/recv_order");
        searchBarForm.setTypes(new String[][]{{"orderNo", "order.no"}, {"customerName", "customer.name"}});
        model.addAttribute("searchBarForm", searchBarForm);

        return "recv_order/find";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String initCreationForm(Model model) {
        List<Location> locationEntities = locationService.findByTypeId(LocationType.CITY);

        ValueAddedOrder order = new ValueAddedOrder();
        order.setServiceTypeId(7L);
        List<TransportedContainer> containers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            containers.add(new TransportedContainer());
        }
        order.setContainers(containers);
        model.addAttribute("order", order);
        return "recv_order/form";
    }

    @RequestMapping(method = RequestMethod.POST, params = "addContainer")
    public String addContainer(@ModelAttribute("order") ValueAddedOrder order,
                               @ModelAttribute("container") TransportedContainer container) {
        logger.debug("order: {}", order);
        logger.debug("container: {}", container);

        order.getContainers().add(container);

        return "recv_order/form";
    }

    @RequestMapping(method = RequestMethod.POST, params = "removeContainer")
    public String removeContainer(@ModelAttribute("order") ValueAddedOrder order,
                                  String bicCode,
                                  final HttpServletRequest request) {
        logger.debug("order: {}", order);
        logger.debug("order: {}", bicCode);

        final int index = Integer.valueOf(request.getParameter("removeContainer"));
        order.getContainers().remove(index);

        return "recv_order/form";
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String save(@Valid @ModelAttribute("order") ValueAddedOrder order,
                       BindingResult bindingResult) {
        logger.debug("order: {}", order);
        order.setStatus(OrderStatus.SAVED.getValue());

        if (bindingResult.hasErrors()) {
            return "recv_order/form";
        } else {
            valueAddedOrderFacade.save(order);
            return "redirect:/recv_order";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "submit")
    public String submit(@Valid @ModelAttribute("order") ValueAddedOrder order,
                         BindingResult bindingResult) {
        logger.debug("order: {}", order);
        order.setStatus(OrderStatus.SUBMITTED.getValue());
        if (bindingResult.hasErrors()) {
            return "recv_order/form";
        } else {
            valueAddedOrderFacade.submit(order);
            return "redirect:/recv_order";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        ReceivingOrderEntity order = recvOrderService.find(id);
        List<Location> locationEntities = locationService.findByTypeId(LocationType.STATION);
        model.addAttribute("locations", locationEntities);
        model.addAttribute("order", order);
        model.addAttribute("tab", "detail");
        return "recv_order/detail";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String getOrder(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        ValueAddedOrder order = valueAddedOrderFacade.find(id);
        logger.debug("order: {}", order);

        List<Location> locationEntities = locationService.findByTypeId(LocationType.STATION);
        model.addAttribute("order", order);
        return "recv_order/form";
    }
}
