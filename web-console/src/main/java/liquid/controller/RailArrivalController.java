package liquid.controller;

import liquid.shipping.domain.RailArrivalDto;
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
 * Time: 3:38 PM
 */
@Controller
@RequestMapping("/task/{taskId}/rail_arrival")
public class RailArrivalController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(RailShippingController.class);

    private static final String TASK_PATH = "rail_arrival";

    @Autowired
    private ShippingContainerService scService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Long orderId = taskService.findOrderId(taskId);
        model.addAttribute("containers", scService.initializeRailContainers(orderId));
        model.addAttribute("rail_task", TASK_PATH);
        return "rail/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long containerId,
                             Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        RailArrivalDto railArrival = scService.findRailArrivalDto(containerId);
        logger.debug("railArrival: {}", railArrival);
        model.addAttribute("container", railArrival);
        return TASK_PATH + "/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @Valid @ModelAttribute("container") RailArrivalDto railArrival,
                         BindingResult bindingResult) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);
        logger.debug("railArrival: {}", railArrival);

        if (bindingResult.hasErrors()) {
            return TASK_PATH + "/edit";
        } else {
            scService.saveRailArrival(railArrival);
        }

        return "redirect:/task/" + taskId + "/" + TASK_PATH;
    }
}
