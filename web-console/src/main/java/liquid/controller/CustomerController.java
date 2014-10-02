package liquid.controller;

import liquid.persistence.domain.CustomerEntity;
import liquid.persistence.repository.CustomerRepository;
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

import javax.validation.Valid;

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

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        int size = 20;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<CustomerEntity> page = customerRepository.findAll(pageRequest);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/customer?");
        model.addAttribute("customer", new CustomerEntity());
        return "customer/page";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String initForm(Model model) {
        model.addAttribute("customer", new CustomerEntity());
        return "customer/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute CustomerEntity customer, BindingResult bindingResult) {
        logger.debug("Customer: {}", customer);

        if (bindingResult.hasErrors()) {
            return "customer/form";
        } else {
            customerRepository.save(customer);
            return "redirect:/customer";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);
        CustomerEntity customer = customerRepository.findOne(id);
        model.addAttribute("customer", customer);
        return "customer/form";
    }
}
