package liquid.controller;

import liquid.order.persistence.domain.ServiceItemEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.order.service.ServiceItemService;
import liquid.service.ServiceSubtypeService;
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
@RequestMapping("/service_item")
public class ServiceItemController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceItemController.class);

    @Autowired
    private ServiceItemService serviceItemService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @ModelAttribute("serviceSubtypes")
    public Iterable<ServiceSubtypeEntity> populateServiceSubtypes() {
        return serviceSubtypeService.findEnabled();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listEnabled(Model model) {
//        Iterable<ServiceItem> serviceItems = serviceItemService.findEnabled();
//        model.addAttribute("serviceItems", serviceItems);
//        model.addAttribute("serviceItem", new ServiceItem());
        return "service/item";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String newServiceSubtype(@ModelAttribute("serviceItem") ServiceItemEntity serviceItem) {
//        serviceItemService.add(serviceItem);
        return "redirect:/service_item";
    }
}
