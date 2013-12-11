package liquid.api.controller;

import liquid.persistence.domain.Customer;
import liquid.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 12/1/13
 * Time: 8:10 PM
 */
@Controller
@RequestMapping("/api/customer")
public class ApiCustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Customer> list() {
        return customerService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, params = "name")
    @ResponseBody
    public Iterable<Customer> listByName(@RequestParam String name) {
        return customerService.findByName(name);
    }
}
