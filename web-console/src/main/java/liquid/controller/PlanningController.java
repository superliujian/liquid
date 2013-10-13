package liquid.controller;

import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
import liquid.service.ChargeService;
import liquid.service.OrderService;
import liquid.service.PlanningService;
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
    private PlanningRepository planningRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChargeTypeRepository ctRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SpRepository spRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ChargeService chargeService;

    public PlanningController() {}

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        Planning planning = planningService.findByTaskId(taskId);

        if (null == planning) {
            planning = new Planning();
            model.addAttribute("planning", planning);
            model.addAttribute("route", new Route());
            return "planning/main";
        } else {
            model.addAttribute("route", new Route());
            return "redirect:/task/" + taskId + "/planning/" + planning.getId();
        }
    }

    @RequestMapping(value = "/{planningId}", method = RequestMethod.GET)
    public String initPlanning(@PathVariable String taskId,
                               @PathVariable String planningId,
                               Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planningId: {}", planningId);

        Planning planning = planningRepository.findOne(Long.valueOf(planningId));
        for (Route route : planning.getRoutes()) {
            Collection<Leg> legs = legRepository.findByRoute(route);
            route.setLegs(legs);
        }
        model.addAttribute("transModes", TransMode.toMap());
        model.addAttribute("planning", planning);
        model.addAttribute("route", new Route());

        model.addAttribute("cts", chargeService.getChargeTypes());
        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("charges", chargeService.findByTaskId(taskId));
        return "planning/main";
    }

    @RequestMapping(method = RequestMethod.POST, params = "createPlanning")
    public String createPlanning(@PathVariable String taskId,
                                 @ModelAttribute("planning") Planning planning,
                                 Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        Order order = orderService.find(bpmService.getOrderIdByTaskId(taskId));
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
                           @Valid Route route,
                           BindingResult result, Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planningId: {}", planningId);
        logger.debug("route: {}", route);

        Planning planning = planningRepository.findOne(Long.valueOf(planningId));
        if (result.hasErrors()) {

            model.addAttribute("planning", planning);
            return "planning/main";
        } else {
            route.setPlanning(planning);
            routeRepository.save(route);
            String redirect = "redirect:/task/" + taskId + "/planning/" + planningId;
            return redirect;
        }
    }

    @RequestMapping(value = "/{planningId}/route/{routeId}/{tab}", method = RequestMethod.GET)
    public String initLeg(@PathVariable String taskId,
                          @PathVariable long planningId,
                          @PathVariable long routeId,
                          @PathVariable String tab,
                          Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planningId: {}", planningId);
        logger.debug("routeId: {}", routeId);
        logger.debug("tab: {}", tab);

        Leg leg = new Leg();

        switch (tab) {
            case "rail":
                List<Location> stationLocs = locationRepository.findByType(LocationType.STATION.getType());
                long defaultDstLocId = computeDefaultDstLocId(stationLocs);
                leg.setDstLocId(defaultDstLocId);
                model.addAttribute("locations", stationLocs);
                break;
            case "barge":
                List<Location> portLocs = locationRepository.findByType(LocationType.PORT.getType());
                defaultDstLocId = computeDefaultDstLocId(portLocs);
                leg.setDstLocId(defaultDstLocId);
                model.addAttribute("sps", spRepository.findByType(SpType.BARGE.getType()));
                model.addAttribute("locations", portLocs);
                break;
            case "vessel":
                portLocs = locationRepository.findByType(LocationType.PORT.getType());
                defaultDstLocId = computeDefaultDstLocId(portLocs);
                leg.setDstLocId(defaultDstLocId);
                model.addAttribute("sps", spRepository.findByType(SpType.VESSEL.getType()));
                model.addAttribute("locations", portLocs);
                break;
            default:
                break;
        }

        model.addAttribute("tab", tab);
        model.addAttribute("routeId", routeId);
        model.addAttribute("leg", leg);
        return "planning/" + tab + "_tab";
    }

    @RequestMapping(value = "/{planningId}/route/{routeId}/{tab}", method = RequestMethod.POST)
    public String addLeg(@PathVariable String taskId,
                         @PathVariable long planningId,
                         @PathVariable long routeId,
                         @PathVariable String tab,
                         Leg leg,
                         Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);
        logger.debug("tab: {}", tab);

        Route route = routeRepository.findOne(routeId);
        Location srcLoc = locationRepository.findOne(leg.getSrcLocId());
        Location dstLoc = locationRepository.findOne(leg.getDstLocId());

        leg.setRoute(route);
        leg.setTransMode(TransMode.valueOf(tab.toUpperCase()).getType());
        if (leg.getSpId() > 0) {
            ServiceProvider sp = spRepository.findOne(leg.getSpId());
            leg.setSp(sp);
        }
        leg.setSrcLoc(srcLoc);
        leg.setDstLoc(dstLoc);
        legRepository.save(leg);
        return "redirect:/task/" + taskId + "/planning/" + planningId;
    }

    @RequestMapping(value = "/{planningId}/route/{routeId}/leg/{legId}", method = RequestMethod.GET)
    public String addLeg(@PathVariable String taskId,
                         @PathVariable long planningId,
                         @PathVariable long routeId,
                         @PathVariable long legId,
                         Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planningId: {}", planningId);
        logger.debug("routeId: {}", routeId);
        logger.debug("legId: {}", legId);

        legRepository.delete(legId);

        return "redirect:/task/" + taskId + "/planning/" + planningId;
    }

    private long computeDefaultDstLocId(List<Location> locations) {
        int size = locations.size();
        long id = 0;
        if (size < 2) {
            id = locations.get(0).getId();
        } else {
            id = locations.get(1).getId();
        }
        return id;
    }
}
