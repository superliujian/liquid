package liquid.controller;

import liquid.persistence.domain.ServiceTypeEntity;
import liquid.service.ServiceTypeService;
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

/**
 * Created by redbrick9 on 4/26/14.
 */
@Controller
@RequestMapping("/service_type")
public class ServiceTypeController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceTypeController.class);

    @Autowired
    private ServiceTypeService serviceTypeService;

    @ModelAttribute("serviceTypes")
    public Iterable<ServiceTypeEntity> findAll() {
        return serviceTypeService.findEnabled();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model) {
        model.addAttribute("serviceType", new ServiceTypeEntity());
        return "data_dict/service_type";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("serviceType") ServiceTypeEntity serviceType,
                         BindingResult bindingResult) {
        logger.debug("serviceType: {}", serviceType);

        if (bindingResult.hasErrors()) {
            return "data_dict/service_type";
        } else {
            serviceTypeService.save(serviceType);
            return "redirect:/service_type";
        }
    }
}
