package liquid.controller;

import liquid.persistence.domain.CustomerEntity;
import liquid.pinyin4j.PinyinHelper;
import liquid.service.CustomerService;
import liquid.web.controller.BaseController;
import liquid.web.domain.SearchBarForm;
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
public class CustomerController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<CustomerEntity> page = customerService.findAll(pageRequest);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/customer?");

        SearchBarForm searchBarForm = new SearchBarForm();
        searchBarForm.setAction("/customer");
        searchBarForm.setTypes(new String[][]{{"name", "customer.name"}});
        model.addAttribute("searchBarForm", searchBarForm);
        return "customer/page";
    }

    @RequestMapping(method = RequestMethod.GET, params = {"search"})
    public String search(@RequestParam(defaultValue = "0", required = false) int number, @ModelAttribute SearchBarForm searchBarForm, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<CustomerEntity> page = customerService.findByQueryNameLike(searchBarForm.getText(), pageRequest);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/customer?");

        searchBarForm.setAction("/customer");
        searchBarForm.setTypes(new String[][]{{"name", "customer.name"}});
        model.addAttribute("searchBarForm", searchBarForm);
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
            if (null == customer.getId()) {
                String queryName = PinyinHelper.converterToFirstSpell(customer.getName()) + ";" + customer.getName();
                customer.setQueryName(queryName);
            }
            customerService.save(customer);
            return "redirect:/customer";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);
        CustomerEntity customer = customerService.find(id);
        model.addAttribute("customer", customer);
        return "customer/form";
    }
}
