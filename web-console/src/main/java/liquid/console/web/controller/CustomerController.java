package liquid.console.web.controller;

import liquid.console.web.persistence.domain.Customer;
import liquid.console.web.persistence.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/24/13
 * Time: 11:26 PM
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerRepository customerRepository;

    @ModelAttribute("customers")
    public Iterable<Customer> populateCustomers() {
        return customerRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void list(Model model, Principal principal) {}

    @RequestMapping(method = RequestMethod.POST)
    public void create(@ModelAttribute("customer") Customer customer, Principal principal) {
        logger.debug("Customer: {}", customer);
        customerRepository.save(customer);
    }
}
