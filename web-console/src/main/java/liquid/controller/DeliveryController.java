package liquid.controller;

import liquid.persistence.domain.RailContainer;
import liquid.persistence.domain.Route;
import liquid.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/16/13
 * Time: 10:28 PM
 */
@Controller
@RequestMapping("/task/{taskId}/delivery")
public class DeliveryController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);

    @Autowired
    private RouteService routeService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        model.addAttribute("routes", routeService.findByTaskId(taskId));
        return "delivery/main";
    }

    @RequestMapping(value = "/{routeId}", method = RequestMethod.GET)
    public String initSave(@PathVariable String taskId,
                           @PathVariable long routeId,
                           Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        Route route = routeService.find(routeId);

        logger.debug("route: {}", route);
        model.addAttribute("route", route);
        return "delivery/edit";
    }

    @RequestMapping(value = "/{routeId}", method = RequestMethod.POST)
    public String save(@PathVariable String taskId,
                       @PathVariable long routeId,
                       @ModelAttribute("route") Route formBean,
                       Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        routeService.save(formBean);

        return "redirect:/task/" + taskId + "/delivery";
    }
}
