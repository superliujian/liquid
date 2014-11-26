package liquid.controller;

import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.web.domain.RailShipping;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.ShippingContainerService;
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
@RequestMapping("/task/{taskId}/rail_shipping")
public class RailShippingController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(RailShippingController.class);

    private static final String TASK_PATH = "rail_shipping";

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private ShipmentService shipmentService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);

        model.addAttribute("rail_task", TASK_PATH);
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        model.addAttribute("shipmentSet", shipmentSet);
        return "rail/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long containerId,
                             Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        RailShipping railShipping = scService.findRailShippingDto(containerId);
        logger.debug("railPlan: {}", railShipping);
        model.addAttribute("container", railShipping);
        return TASK_PATH + "/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @Valid @ModelAttribute("container") RailShipping railShipping,
                         BindingResult bindingResult) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);
        logger.debug("railShipping: {}", railShipping);

        if (bindingResult.hasErrors()) {
            return TASK_PATH + "/edit";
        } else {
            scService.saveRailShipping(railShipping);
        }

        return "redirect:/task/" + taskId + "/" + TASK_PATH;
    }
}
