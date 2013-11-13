package liquid.controller;

import liquid.metadata.ContainerCap;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        model.addAttribute("containerTypeMap", ContainerCap.toMap());
        model.addAttribute("routes", routes);
        return "allocation/main";
    }

    @RequestMapping(value = "/route/{routeId}/sc", method = RequestMethod.GET)
    public String initAllocation(@PathVariable String taskId,
                                 @PathVariable long routeId,
                                 Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        ShippingContainer sc = new ShippingContainer();

        // Set up pickup contact and his phone by default
        Route route = routeService.find(routeId);
        Collection<ShippingContainer> scs = scService.findByRoute(route);
        if (scs.iterator().hasNext()) {
            ShippingContainer firstOne = scs.iterator().next();
            sc.setPickupContact(firstOne.getPickupContact());
            sc.setContactPhone(firstOne.getContactPhone());
        }

        model.addAttribute("sc", sc);
        model.addAttribute("routeId", routeId);
        model.addAttribute("containers", containerService.findAllInStock(route.getPlanning().getOrder().getContainerType()));
        return "allocation/allocating";
    }

    @RequestMapping(value = "/route/{routeId}/sc", method = RequestMethod.POST)
    public String allocate(@PathVariable String taskId,
                           @PathVariable long routeId,
                           ShippingContainer sc,
                           BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        if (bindingResult.hasErrors()) {
            return "allocation/allocating";
        }

        scService.add(routeId, sc);

        Collection<Route> routes = routeService.findByTaskId(taskId);
        model.addAttribute("routes", routes);
        return "redirect:/task/" + taskId + "/allocation";
    }

    // TODO: change to form submit.
    @RequestMapping(value = "/route/{routeId}/sc/{scId}", method = RequestMethod.GET)
    public String remove(@PathVariable String taskId,
                         @PathVariable long routeId,
                         @PathVariable long scId,
                         Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        scService.remove(scId);

        Collection<Route> routes = routeService.findByTaskId(taskId);
        model.addAttribute("routes", routes);
        return "redirect:/task/" + taskId + "/allocation";
    }
}
