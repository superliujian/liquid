package liquid.controller;

import liquid.persistence.domain.RailContainer;
import liquid.service.ShippingContainerService;
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
 * Date: 10/3/13
 * Time: 9:42 PM
 */
@Controller
@RequestMapping("/task/{taskId}/rail")
public class RailController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(AllocationController.class);

    @Autowired
    private ShippingContainerService scService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        model.addAttribute("containers", scService.initialize(taskId));
        return "rail/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long containerId,
                             Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        RailContainer railContainer = scService.findRailContainer(containerId);
        logger.debug("railContainer: {}", railContainer);
        model.addAttribute("container", railContainer);
        return "rail/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @ModelAttribute("container") RailContainer formBean,
                         Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        scService.saveRailContainer(containerId, formBean);

        return "redirect:/task/" + taskId + "/rail";
    }
}
