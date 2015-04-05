package liquid.operation.restfulapi;

import liquid.operation.domain.Customer;
import liquid.operation.service.InternalCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * TODO: Comments.
 * User: tao
 * Date: 12/1/13
 * Time: 8:10 PM
 */
@Controller
@RequestMapping("/api/customer")
public class CustomerRestController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerRestController.class);

    @Autowired
    private InternalCustomerService customerService;

    @RequestMapping(method = RequestMethod.GET, params = "name")
    @ResponseBody
    public Iterable<Customer> listByName(@RequestParam String name) {
        logger.debug("name: {}", name);
        return customerService.findByNameLike(name);
    }
}
