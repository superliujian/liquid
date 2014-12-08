package liquid.controller;

import liquid.metadata.ContainerCap;
import liquid.container.domain.ContainerType;
import liquid.domain.LocationType;
import liquid.order.domain.OrderStatus;
import liquid.order.persistence.domain.ReceivingOrder;
import liquid.order.service.ReceivingOrderService;
import liquid.persistence.domain.*;
import liquid.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 4:33 PM
 */
@Controller
@RequestMapping("/recv_order")
public class ReceivingOrderController {
    private static Logger logger = LoggerFactory.getLogger(ReceivingOrderController.class);

    @Autowired
    private ReceivingOrderService recvOrderService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CargoTypeService cargoTypeService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @ModelAttribute("serviceTypes")
    public Iterable<ServiceTypeEntity> populateServiceTypes() {
        return serviceTypeService.findAll();
    }

    @ModelAttribute("customers")
    public Iterable<CustomerEntity> populateCustomers() {
        return customerService.findAll();
    }

    @ModelAttribute("cargos")
    public Iterable<GoodsEntity> populateCargoTypes() {
        return cargoTypeService.findAll();
    }

    @ModelAttribute("locations")
    public Iterable<LocationEntity> populateLocations() {
        return locationService.findByType(LocationType.CITY.getType());
    }

    @ModelAttribute("containerTypes")
    public ContainerType[] populateContainerTypes() {
        return ContainerType.values();
    }

    @ModelAttribute("containerCaps")
    public ContainerCap[] populateContainerCaps() {
        return ContainerCap.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initFind(Model model) {
        model.addAttribute("orders", recvOrderService.findAllOrderByDesc());
        return "recv_order/find";
    }

    @RequestMapping(method = RequestMethod.GET, params = "findByOrderNo")
    public String findById(@RequestParam String param, Model model, Principal principal) {
        logger.debug("param: {}", param);

        model.addAttribute("orders", recvOrderService.findByOrderNo(param));
        return "recv_order/find";
    }

    @RequestMapping(method = RequestMethod.GET, params = "findByCustomerName")
    public String findByCustomerName(@RequestParam String param, Model model, Principal principal) {
        logger.debug("param: {}", param);

        model.addAttribute("orders", recvOrderService.findByCustomerName(param));
        return "recv_order/find";
    }

    @RequestMapping(method = RequestMethod.GET, params = "add")
    public String initCreationForm(Model model) {
        List<LocationEntity> locationEntities = locationService.findByType(LocationType.CITY.getType());

        ReceivingOrder order = recvOrderService.newOrder(locationEntities);

        model.addAttribute("order", order);
        return "recv_order/form";
    }

    @RequestMapping(method = RequestMethod.POST, params = "addContainer")
    public String addContainer(@ModelAttribute("order") ReceivingOrder order,
                               String bicCode) {
        logger.debug("order: {}", order);
        logger.debug("order: {}", bicCode);

        order.getBicCodes().add(bicCode);

        return "recv_order/form";
    }

    @RequestMapping(method = RequestMethod.POST, params = "removeContainer")
    public String removeContainer(@ModelAttribute("order") ReceivingOrder order,
                                  String bicCode,
                                  final HttpServletRequest request) {
        logger.debug("order: {}", order);
        logger.debug("order: {}", bicCode);

        final int index = Integer.valueOf(request.getParameter("removeContainer"));
        order.getBicCodes().remove(index);

        return "recv_order/form";
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String save(@Valid @ModelAttribute("order") ReceivingOrder order,
                       BindingResult bindingResult) {
        logger.debug("order: {}", order);
        order.setStatus(OrderStatus.SAVED.getValue());

        if (bindingResult.hasErrors()) {
            return "recv_order/form";
        } else {
            recvOrderService.save(order);
            return "redirect:/recv_order";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable long id,
                         Model model, Principal principal) {
        logger.debug("id: {}", id);

        ReceivingOrder order = recvOrderService.find(id);
        List<LocationEntity> locationEntities = locationService.findByType(LocationType.STATION.getType());
        model.addAttribute("locations", locationEntities);
        model.addAttribute("order", order);
        model.addAttribute("tab", "detail");
        return "recv_order/detail";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, params = "action")
    public String getOrder(@PathVariable long id, @RequestParam String action,
                           Model model, Principal principal) {
        logger.debug("id: {}", id);
        logger.debug("action: {}", action);

        ReceivingOrder order = recvOrderService.find(id);
        logger.debug("order: {}", order);

        List<LocationEntity> locationEntities = locationService.findByType(LocationType.STATION.getType());
        model.addAttribute("order", order);
        return "recv_order/form";
    }

    @RequestMapping(method = RequestMethod.POST, params = "submit")
    public String submit(@Valid @ModelAttribute("order") ReceivingOrder order,
                         BindingResult bindingResult) {
        logger.debug("order: {}", order);
        order.setStatus(OrderStatus.SUBMITTED.getValue());
        if (bindingResult.hasErrors()) {
            return "recv_order/form";
        } else {
            recvOrderService.save(order);
            return "redirect:/recv_order";
        }
    }
}
