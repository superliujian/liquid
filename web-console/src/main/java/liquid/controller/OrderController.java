package liquid.controller;

import liquid.context.BusinessContext;
import liquid.persistence.domain.*;
import liquid.persistence.repository.CargoRepository;
import liquid.persistence.repository.CustomerRepository;
import liquid.persistence.repository.OrderRepository;
import liquid.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 2:38 PM
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BusinessContext businessContext;

    @ModelAttribute("orders")
    public Iterable<Order> populateOrders() {
        return orderRepository.findAll();
    }

    @ModelAttribute("customers")
    public Iterable<Customer> populateCustomers() {
        return customerRepository.findAll();
    }

    @ModelAttribute("cargos")
    public Iterable<Cargo> populateCargos() {
        return cargoRepository.findAll();
    }

    @ModelAttribute("tradeTypes")
    public TradeType[] populateTradeTypes() {
        return TradeType.values();
    }

    @ModelAttribute("loadingTypes")
    public LoadingType[] populateLoadings() {
        return LoadingType.values();
    }

    @ModelAttribute("status")
    public OrderStatus[] populateStatus() {
        return OrderStatus.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void list(Model model, Principal principal) {}

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public void form(Model model, Principal principal) {
        model.addAttribute("order", new Order());
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String save(@Valid @ModelAttribute Order order,
                       BindingResult bindingResult, Principal principal) {
        logger.debug("order: {}", order);
        order.setStatus(OrderStatus.SAVED.getValue());

        if (bindingResult.hasErrors()) {
            return "order/form";
        } else {
            orderService.save(order);
            return "redirect:/order";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "submit")
    public String submit(@Valid @ModelAttribute Order order,
                         BindingResult bindingResult, Principal principal) {
        // TODO: add to interceptor.
        businessContext.setUsername(principal.getName());

        logger.debug("order: {}", order);
        order.setStatus(OrderStatus.SUBMITTED.getValue());
        if (bindingResult.hasErrors()) {
            return "order/form";
        } else {
            orderService.submit(order);
            return "redirect:/order";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getOrder(@PathVariable long id, @RequestParam String action,
                           Model model, Principal principal) {
        logger.debug("id: {}", id);
        logger.debug("action: {}", action);
        Order order = orderRepository.findOne(id);
        // TODO: looking for the better way to do that
        order.setCustomerId(order.getCustomer().getId());
        order.setCargoId(order.getCargo().getId());
        logger.debug("order: {}", order);
        model.addAttribute("order", order);
        return "order/form";
    }
}
