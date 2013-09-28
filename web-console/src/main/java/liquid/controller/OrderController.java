package liquid.controller;

import liquid.persistence.domain.*;
import liquid.persistence.repository.CargoRepository;
import liquid.persistence.repository.CustomerRepository;
import liquid.persistence.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

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

    @RequestMapping(method = RequestMethod.GET)
    public void list(Model model, Principal principal) {}

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public void form(Model model, Principal principal) {
        model.addAttribute("order", new Order());
        model.addAttribute("trades", Trade.defaultTrades());
        model.addAttribute("loadings", Loading.defaultLoadings());
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute Order order, BindingResult bindingResult, Principal principal) {
        logger.debug("Order: {}", order);

        if (bindingResult.hasErrors()) {
            return "order";
        } else {
            orderRepository.save(order);
            return "redirect:/order";
        }
    }
}
