package liquid.operation.controller;

import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.ServiceProviderService;
import liquid.operation.service.ServiceSubtypeService;
import liquid.web.controller.BaseController;
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
 * Date: 10/2/13
 * Time: 5:02 PM
 */
@Controller
@RequestMapping("/sp")
public class ServiceProviderController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderController.class);

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ServiceProvider> page = serviceProviderService.findAll(pageRequest);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/sp?");
        return "sp/sp";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String initForm(Model model) {
        model.addAttribute("spTypes", serviceProviderService.getSpTypes());
        model.addAttribute("serviceSubtypes", serviceSubtypeService.findEnabled());

        model.addAttribute("sp", new ServiceProvider());
        return "sp/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        model.addAttribute("spTypes", serviceProviderService.getSpTypes());
        model.addAttribute("serviceSubtypes", serviceSubtypeService.findEnabled());

        ServiceProvider sp = serviceProviderService.find(id);
        model.addAttribute("sp", sp);
        return "sp/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("sp") ServiceProvider sp, BindingResult bindingResult) {
        logger.debug("sp: {}", sp);

        if (bindingResult.hasErrors()) {
            return "sp/sp";
        } else {
            serviceProviderService.save(sp);
            return "redirect:/sp";
        }
    }
}