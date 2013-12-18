package liquid.controller;

import liquid.persistence.domain.BargeContainer;
import liquid.persistence.domain.Route;
import liquid.service.RouteService;
import liquid.service.ShippingContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 3:33 PM
 */
@Controller
@RequestMapping("/task/{taskId}/barge")
public class BargeController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(BargeController.class);

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private RouteService routeService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        Collection<Route> routes = routeService.findByTaskId(taskId);
        model.addAttribute("routes", routes);
        return "barge/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long containerId,
                             Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        BargeContainer bargeContainer = scService.findBargeContainer(containerId);
        logger.debug("bargeContainer: {}", bargeContainer);
        model.addAttribute("container", bargeContainer);
        return "barge/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @ModelAttribute("container") BargeContainer formBean,
                         BindingResult bindingResult, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        if (bindingResult.hasErrors()) {
            return "barge/edit";
        } else {
            scService.saveBargeContainer(containerId, formBean);
            return "redirect:/task/" + taskId + "/barge";
        }
    }
}
