package liquid.order.restfulapi;

import liquid.operation.domain.Customer;
import liquid.operation.service.CustomerService;
import liquid.order.domain.ReceivingOrderEntity;
import liquid.order.service.ReceivingOrderServiceImpl;
import liquid.web.domain.SearchBarForm;
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
 * Created by Tao Ma on 2/25/15.
 */
@Controller
@RequestMapping("/api/value_added_order")
public class ApiValueAddedOrderController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReceivingOrderServiceImpl receivingOrderService;

    @RequestMapping(method = RequestMethod.GET, params = "text")
    @ResponseBody
    public Iterable<SearchBarForm> listByName(@RequestParam String text) {
        Iterable<Customer> customers = customerService.findByQueryNameLike(text);
        List<SearchBarForm> searchBarForms = new ArrayList<>();
        for (Customer customer : customers) {
            SearchBarForm searchBarForm = new SearchBarForm();
            searchBarForm.setType("customer");
            searchBarForm.setId(customer.getId());
            searchBarForm.setText(customer.getName());
            searchBarForms.add(searchBarForm);
        }

        PageRequest pageRequest = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "id"));
        Page<ReceivingOrderEntity> page = receivingOrderService.findByOrderNoLike(text, pageRequest);
        for (ReceivingOrderEntity order : page.getContent()) {
            SearchBarForm searchBarForm = new SearchBarForm();
            searchBarForm.setId(order.getId());
            searchBarForm.setType("order");
            searchBarForm.setText(order.getOrderNo());
            searchBarForms.add(searchBarForm);
        }

        return searchBarForms;
    }
}
