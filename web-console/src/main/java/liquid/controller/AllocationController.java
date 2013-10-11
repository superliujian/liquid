package liquid.controller;

import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
import liquid.service.*;
import liquid.service.bpm.ActivitiEngineService;
import org.activiti.engine.task.Task;
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
import java.util.Collection;
import java.util.List;

/**
 * Container allocation controller.
 * User: tao
 * Date: 10/3/13
 * Time: 2:42 PM
 */
@Controller
@RequestMapping("/task/{taskId}/allocation")
public class AllocationController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(AllocationController.class);

    @Autowired
    private RouteService routeService;

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private ContainerService containerService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model, Principal principal) {
        Collection<Route> routes = routeService.findByTaskId(taskId);
        model.addAttribute("routes", routes);
        return "allocation/main";
    }

    @RequestMapping(value = "/route/{routeId}/sc", method = RequestMethod.GET)
    public String initAllocation(@PathVariable String taskId,
                                 @PathVariable long routeId,
                                 Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

//        scService.add(routeId, sc);

        model.addAttribute("sc", new ShippingContainer());
        model.addAttribute("routeId", routeId);
        model.addAttribute("containers", containerService.findAll());
        return "allocation/allocating";
    }
}
