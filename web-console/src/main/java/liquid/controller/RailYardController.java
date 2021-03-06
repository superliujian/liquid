package liquid.controller;

import liquid.process.controller.BaseTaskController;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.ShippingContainerService;
import liquid.transport.web.domain.RailYard;
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
 * Time: 1:04 PM
 */
@Controller
@RequestMapping("/task/{taskId}/rail_yard")
public class RailYardController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(RailYardController.class);

    private static final String TASK_PATH = "rail_yard";

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private ShipmentService shipmentService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        model.addAttribute("containers", scService.initializeRailContainers(orderId));
        model.addAttribute("rail_task", TASK_PATH);

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

        RailYard railYard = scService.findRailYardDto(containerId);
        logger.debug("railYard: {}", railYard);
        model.addAttribute("container", railYard);
        return TASK_PATH + "/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @Valid @ModelAttribute("container") RailYard railYard,
                         BindingResult bindingResult) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);
        logger.debug("railYard: {}", railYard);

        if (bindingResult.hasErrors()) {
            return TASK_PATH + "/edit";
        } else {
            scService.saveRailYard(railYard);
        }

        return "redirect:/task/" + taskId + "/" + TASK_PATH;
    }
}
