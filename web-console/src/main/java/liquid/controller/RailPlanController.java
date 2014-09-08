package liquid.controller;

import liquid.shipping.domain.RailPlanDto;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.service.RouteService;
import liquid.shipping.service.ShippingContainerService;
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

import javax.validation.Valid;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/19/13
 * Time: 2:27 PM
 */
@Controller
@RequestMapping("/task/{taskId}/rail_plan")
public class RailPlanController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(RailPlanController.class);

    private static final String TASK_PATH = "rail_plan";

    @Autowired
    private RouteService routeService;

    @Autowired
    private ShippingContainerService scService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        model.addAttribute("containers", scService.initializeRailContainers(orderId));
        model.addAttribute("rail_task", TASK_PATH);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);
        model.addAttribute("routes", routes);
        return "rail/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long containerId,
                             Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        RailPlanDto railPlan = scService.findRailPlanDto(containerId);
        logger.debug("railPlan: {}", railPlan);
        model.addAttribute("container", railPlan);
        return TASK_PATH + "/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @Valid @ModelAttribute("container") RailPlanDto railPlan,
                         BindingResult bindingResult) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);
        logger.debug("railPlan: {}", railPlan);

        if (bindingResult.hasErrors()) {
            return TASK_PATH + "/edit";
        } else {
            scService.saveRailPlan(railPlan);
        }

        return "redirect:/task/" + taskId + "/" + TASK_PATH;
    }

}
