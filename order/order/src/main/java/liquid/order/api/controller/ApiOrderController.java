package liquid.order.api.controller;

import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderServiceImpl;
import liquid.persistence.domain.CustomerEntity;
import liquid.service.CustomerService;
import liquid.web.domain.SearchBarForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 12/9/14.
 */
@Controller
@RequestMapping("/api/order")
public class ApiOrderController {
    private static final Logger logger = LoggerFactory.getLogger(ApiOrderController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderServiceImpl orderService;

    @RequestMapping(method = RequestMethod.GET, params = "text")
    @ResponseBody
    public Iterable<SearchBarForm> listByName(@RequestParam String text) {
        logger.debug("text: {}", text);
        Iterable<CustomerEntity> customers = customerService.findByQueryNameLike(text);
        List<SearchBarForm> searchBarForms = new ArrayList<>();
        for (CustomerEntity customer : customers) {
            SearchBarForm searchBarForm = new SearchBarForm();
            searchBarForm.setType("customer");
            searchBarForm.setId(customer.getId());
            searchBarForm.setText(customer.getName());
            searchBarForms.add(searchBarForm);
        }

        PageRequest pageRequest = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "id"));
        Page<OrderEntity> page = orderService.findByOrderNoLike(text, pageRequest);
        for (OrderEntity order : page.getContent()) {
            SearchBarForm searchBarForm = new SearchBarForm();
            searchBarForm.setId(order.getId());
            searchBarForm.setType("order");
            searchBarForm.setText(order.getOrderNo());
            searchBarForms.add(searchBarForm);
        }

        return searchBarForms;
    }
}
