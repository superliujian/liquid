package liquid.controller;

import liquid.dto.TruckDto;
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

import javax.validation.Valid;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/19/13
 * Time: 12:08 AM
 */
@Controller
@RequestMapping("/task/{taskId}/truck")
public class TruckController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(TruckController.class);

    private static final String TASK_PATH = "truck";

    @Autowired
    private ShippingContainerService scService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);

        model.addAttribute("containers", scService.initialize(taskId));
        return TASK_PATH + "/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long containerId,
                             Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        TruckDto truck = scService.findTruckDto(containerId);
        logger.debug("truck: {}", truck);
        model.addAttribute("truck", truck);
        return TASK_PATH + "/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @Valid @ModelAttribute("truck") TruckDto truck,
                         BindingResult bindingResult) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);
        logger.debug("truck: {}", truck);

        if (bindingResult.hasErrors()) {
            return TASK_PATH + "/edit";
        } else {
            scService.saveTruck(truck);
        }

        return "redirect:/task/" + taskId + "/" + TASK_PATH;
    }
}
