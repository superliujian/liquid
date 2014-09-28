package liquid.controller;

import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.domain.VesselContainerEntity;
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

import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 4:28 PM
 */
@Controller
@RequestMapping("/task/{taskId}/vessel")
public class VesselController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(VesselController.class);

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private RouteService routeService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        scService.initVesselContainers(orderId);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);
        model.addAttribute("routes", routes);
        return "vessel/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long containerId,
                             Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        VesselContainerEntity vesselContainer = scService.findVesselContainer(containerId);
        logger.debug("vesselContainer: {}", vesselContainer);
        model.addAttribute("container", vesselContainer);
        return "vessel/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @ModelAttribute("container") VesselContainerEntity formBean,
                         BindingResult bindingResult, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        if (bindingResult.hasErrors()) {
            return "vessel/edit";
        } else {
            scService.saveVesselContainer(containerId, formBean);
            return "redirect:/task/" + taskId + "/vessel";
        }
    }
}
