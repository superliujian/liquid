package liquid.controller;

import liquid.process.controller.BaseTaskController;
import liquid.transport.service.ShippingContainerService;
import liquid.transport.persistence.domain.BargeContainerEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.service.ShipmentService;
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
    private ShipmentService shipmentService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        scService.initBargeContainers(orderId);

        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        model.addAttribute("shipmentSet", shipmentSet);
        return "barge/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long containerId,
                             Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        BargeContainerEntity bargeContainer = scService.findBargeContainer(containerId);
        logger.debug("bargeContainer: {}", bargeContainer);
        model.addAttribute("container", bargeContainer);
        return "barge/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @ModelAttribute("container") BargeContainerEntity formBean,
                         BindingResult bindingResult) {
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
