package liquid.controller;

import liquid.metadata.ChargeWay;
import liquid.metadata.TransMode;
import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
import liquid.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 12:04 PM
 */
@Controller
@RequestMapping("/task/{taskId}/planning")
public class PlanningController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(PlanningController.class);

    @Autowired
    private PlanningService planningService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ChargeService chargeService;

    public PlanningController() {
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        Planning planning = planningService.findByTaskId(taskId);

        if (null == planning) {
            planning = new Planning();
            model.addAttribute("planning", planning);
            model.addAttribute("route", new RouteEntity());
            return "planning/main";
        } else {
            model.addAttribute("route", new RouteEntity());
            return "redirect:/task/" + taskId + "/planning/" + planning.getId();
        }
    }

    @RequestMapping(value = "/{planningId}", method = RequestMethod.GET)
    public String initPlanning(@PathVariable String taskId,
                               @PathVariable String planningId,
                               Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planningId: {}", planningId);

        int containerUsage = 0;
        Planning planning = planningService.findOne(Long.valueOf(planningId));
        for (RouteEntity route : planning.getRoutes()) {
            containerUsage += route.getContainerQty();
            Collection<Leg> legs = legRepository.findByRoute(route);
            route.setLegs(legs);
        }
        model.addAttribute("transModes", TransMode.toMap());
        model.addAttribute("planning", planning);

        RouteEntity route = new RouteEntity();
        route.setContainerQty(planning.getOrder().getContainerQty() - containerUsage);
        model.addAttribute("route", route);

        model.addAttribute("cts", chargeService.getChargeTypes());
        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<Charge> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "planning/main";
    }

    @RequestMapping(method = RequestMethod.POST, params = "createPlanning")
    public String createPlanning(@PathVariable String taskId,
                                 @ModelAttribute("planning") Planning planning,
                                 Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        OrderEntity order = orderService.find(taskService.getOrderIdByTaskId(taskId));
        planning.setOrder(order);
        Planning newOne = planningRepository.save(planning);
        model.addAttribute("planning", newOne);
        return "redirect:/task/" + taskId + "/planning/" + newOne.getId();
    }

    @RequestMapping(value = "/{planningId}", method = RequestMethod.POST, params = "deletePlanning")
    public String deletePlanning(@PathVariable String taskId,
                                 @PathVariable String planningId,
                                 Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        planningRepository.delete(Long.valueOf(planningId));

        return "redirect:/task/" + taskId + "/planning";
    }

    @RequestMapping(value = "/{planningId}", method = RequestMethod.POST, params = "applyMulti")
    public String applyMulti(@PathVariable String taskId,
                             @PathVariable String planningId,
                             Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planningId: {}", planningId);

        planningService.editPlanning(taskId, Long.valueOf(planningId), false);

        String redirect = "redirect:/task/" + taskId + "/planning/" + planningId;
        return redirect;
    }

    @RequestMapping(value = "/{planningId}", method = RequestMethod.POST, params = "applySame")
    public String applySame(@PathVariable String taskId,
                            @PathVariable String planningId,
                            Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planningId: {}", planningId);

        planningService.editPlanning(taskId, Long.valueOf(planningId), true);

        String redirect = "redirect:/task/" + taskId + "/planning/" + planningId;
        return redirect;
    }

    @RequestMapping(value = "/{planningId}/route", method = RequestMethod.POST)
    public String addRoute(@PathVariable String taskId,
                           @PathVariable String planningId,
                           @Valid RouteEntity route,
                           BindingResult result, Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planningId: {}", planningId);
        logger.debug("route: {}", route);

        Planning planning = planningService.findOne(Long.valueOf(planningId));

        int containerUsage = 0;
        Collection<RouteEntity> routes = planning.getRoutes();
        for (RouteEntity r : planning.getRoutes()) {
            containerUsage += r.getContainerQty();
            Collection<Leg> legs = legRepository.findByRoute(r);
            r.setLegs(legs);
        }

        if (result.hasErrors()) {
            model.addAttribute("planning", planning);
            return "planning/main";
        } else if (route.getContainerQty() > (planning.getOrder().getContainerQty() - containerUsage)) {
            setFieldError(result, "route", "containerQty", route.getContainerQty(), (planning.getOrder().getContainerQty() - containerUsage));
            model.addAttribute("transModes", TransMode.toMap());
            model.addAttribute("planning", planning);
            return "planning/main";
        } else {
            routeService.save(route, planning);
            String redirect = "redirect:/task/" + taskId + "/planning/" + planningId;
            return redirect;
        }
    }
}
