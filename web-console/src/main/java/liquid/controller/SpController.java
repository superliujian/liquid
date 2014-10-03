package liquid.controller;

import liquid.domain.ServiceProvider;
import liquid.facade.ServiceProviderFacade;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.service.ChargeService;
import liquid.service.ServiceProviderService;
import liquid.service.ServiceSubtypeService;
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
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 5:02 PM
 */
@Controller
@RequestMapping("/sp")
public class SpController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SpController.class);

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ServiceProviderFacade serviceProviderFacade;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private ChargeService chargeService;

    @ModelAttribute("spTypes")
    public Map<Long, String> populateSpTypes() {
        return serviceProviderService.getSpTypes();
    }

    @ModelAttribute("serviceSubtypes")
    public Iterable<ServiceSubtypeEntity> populateServiceSubtypes() {
        return serviceSubtypeService.findEnabled();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ServiceProviderEntity> page = serviceProviderService.findAll(pageRequest);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/sp?");
        return "sp/sp";
    }

    @RequestMapping(method = RequestMethod.GET, params = "type")
    @ResponseBody
    public List<ServiceProviderEntity> list(@RequestParam long type) {
        logger.debug("type: {}", type);
        return serviceSubtypeService.find(type).getServiceProviders();
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String initForm(Model model, Principal principal) {
        model.addAttribute("sp", new ServiceProvider());
        return "sp/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("sp") ServiceProvider sp, BindingResult bindingResult) {
        logger.debug("sp: {}", sp);

        if (bindingResult.hasErrors()) {
            return "sp/sp";
        } else {
            serviceProviderFacade.save(sp);
            return "redirect:/sp";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);
        ServiceProvider sp = serviceProviderFacade.find(id);
        model.addAttribute("sp", sp);
        return "sp/form";
    }
}