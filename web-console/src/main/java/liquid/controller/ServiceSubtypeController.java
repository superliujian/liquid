package liquid.controller;

import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceSubtypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by redbrick9 on 5/9/14.
 */
@Controller
@RequestMapping("/service_subtype")
public class ServiceSubtypeController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceSubtypeController.class);

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @RequestMapping(method = RequestMethod.GET)
    public String listEnabled(Model model) {
        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        model.addAttribute("serviceSubtype", new ServiceSubtype());
        return "service/subtype";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String newServiceSubtype(@ModelAttribute("serviceSubtype") ServiceSubtype serviceSubtype) {
        serviceSubtypeService.add(serviceSubtype);
        return "redirect:/service_subtype";
    }
}
