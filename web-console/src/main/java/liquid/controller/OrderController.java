package liquid.controller;

import liquid.context.BusinessContext;
import liquid.metadata.*;
import liquid.persistence.domain.*;
import liquid.service.*;
import liquid.service.bpm.ActivitiEngineService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 2:38 PM
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseChargeController {
    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CargoTypeService cargoTypeService;

    @Autowired
    private BusinessContext businessContext;

    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private ChargeService chargeService;

    @ModelAttribute("customers")
    public Iterable<Customer> populateCustomers() {
        return customerService.findAll();
    }

    @ModelAttribute("cargos")
    public Iterable<Cargo> populateCargos() {
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

    @ModelAttribute("containerTypes")
    public ContainerType[] populateContainerTypes() {
        return ContainerType.values();
    }

    @ModelAttribute("containerCaps")
    public ContainerCap[] populateContainerCaps() {
        return ContainerCap.values();
    }

    @ModelAttribute("status")
    public OrderStatus[] populateStatus() {
        return OrderStatus.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initFind(Model model, Principal principal) {
        model.addAttribute("orders", orderService.findAllOrderByDesc());
        return "order/find";
    }

    @RequestMapping(method = RequestMethod.GET, params = "findById")
    public String findById(@RequestParam String param, Model model, Principal principal) {
        logger.debug("param: {}", param);
        if (null == param || param.trim().length() == 0) {} else {
            try {
                model.addAttribute("orders", orderService.find(Long.parseLong(param)));
                return "order/find";
            } catch (Exception e) {
                logger.warn("An exception was thrown when calling findById", e);
            }
        }
        model.addAttribute("orders", orderService.findAllOrderByDesc());
        return "order/find";
    }

    @RequestMapping(method = RequestMethod.GET, params = "findByCustomerName")
    public String findByCustomerName(@RequestParam String param, Model model, Principal principal) {
        logger.debug("param: {}", param);

        model.addAttribute("orders", orderService.findByCustomerName(param));
        return "order/find";
    }

    @RequestMapping(method = RequestMethod.GET, params = "add")
    public String initCreationForm(Model model, Principal principal) {
        List<Location> locations = locationService.findByType(LocationType.CITY.getType());

        Order order = orderService.newOrder(locations);

        model.addAttribute("locations", locations);
        model.addAttribute("order", order);
        return "order/form";
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String save(@Valid @ModelAttribute Order order,
                       BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("order: {}", order);
        order.setStatus(OrderStatus.SAVED.getValue());

        if (bindingResult.hasErrors()) {
            List<Location> locations = locationService.findByType(LocationType.CITY.getType());
            model.addAttribute("locations", locations);
            return "order/form";
        } else {
            orderService.save(order);
            return "redirect:/order";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "submit")
    public String submit(@Valid @ModelAttribute Order order,
                         BindingResult bindingResult, Model model, Principal principal) {
        // TODO: add to interceptor.
        businessContext.setUsername(principal.getName());

        logger.debug("order: {}", order);
        order.setStatus(OrderStatus.SUBMITTED.getValue());
        if (bindingResult.hasErrors()) {
            List<Location> locations = locationService.findByType(LocationType.CITY.getType());
            model.addAttribute("locations", locations);
            return "order/form";
        } else {
            orderService.submit(order);
            return "redirect:/order";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable long id,
                         Model model, Principal principal) {
        logger.debug("id: {}", id);

        Order order = orderService.find(id);
        List<Location> locations = locationService.findByType(LocationType.STATION.getType());
        model.addAttribute("locations", locations);
        model.addAttribute("order", order);
        model.addAttribute("tab", "detail");
        return "order/detail";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, params = "action")
    public String getOrder(@PathVariable long id, @RequestParam String action,
                           Model model, Principal principal) {
        logger.debug("id: {}", id);
        logger.debug("action: {}", action);

        Order order = orderService.find(id);
        logger.debug("order: {}", order);

        List<Location> locations = locationService.findByType(LocationType.STATION.getType());

        model.addAttribute("locations", locations);
        model.addAttribute("order", order);
        return "order/form";
    }

    @RequestMapping(value = "/{id}/{tab}", method = RequestMethod.GET)
    public String charge(@PathVariable long id,
                         @PathVariable String tab,
                         Model model, Principal principal) {
        logger.debug("id: {}", id);
        logger.debug("tab: {}", tab);

        Order order = orderService.find(id);

        switch (tab) {
            case "task":
                List<Task> tasks = bpmService.listTasksByOrderId(id);
                model.addAttribute("tasks", tasks);
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
