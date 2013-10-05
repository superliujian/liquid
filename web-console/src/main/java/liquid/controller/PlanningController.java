package liquid.controller;

import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
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
    private OrderRepository orderRepository;

    @Autowired
    private TransRailwayRepository transRailwayRepository;

    @Autowired
    private TransBargeRepository transBargeRepository;

    @Autowired
    private TransVesselRepository transVesselRepository;

    @Autowired
    private ChargeTypeRepository ctRepository;

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SpRepository spRepository;

    @ModelAttribute("chargeWays")
    public ChargeWay[] populateChargeWays() {
        return ChargeWay.values();
    }

    @ModelAttribute("cts")
    public Map<Long, String> populateChargeTypes() {
        Map<Long, String> cts = new TreeMap<Long, String>();
        Iterable<ChargeType> iterable = ctRepository.findAll();
        for (ChargeType chargeType : iterable) {
            cts.put(chargeType.getId(), chargeType.getName());
        }
        return cts;
    }

    @ModelAttribute("charges")
    public Iterable<Charge> populateCharges(@PathVariable String taskId) {
        return chargeRepository.findByTaskId(taskId);
    }

    @ModelAttribute("railways")
    public Iterable<TransRailway> populateRailways(@PathVariable String taskId) {
        return transRailwayRepository.findByTaskId(taskId);
    }

    @ModelAttribute("barges")
    public Iterable<TransBarge> populateBarges(@PathVariable String taskId) {
        return transBargeRepository.findByTaskId(taskId);
    }

    @ModelAttribute("vessels")
    public Iterable<TransVessel> populateVessels(@PathVariable String taskId) {
        return transVesselRepository.findByTaskId(taskId);
    }

    /**
     * {taskId}, {tab} are required by tab part of template.
     *
     * @param taskId
     * @param tab
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = "/{tab}", method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       @PathVariable String tab,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("tab: {}", tab);
        model.addAttribute("tab", tab);

        Order order = orderRepository.findOne(bpmService.getOrderIdByTaskId(taskId));

        switch (tab) {
            case "railway":
            case "barge":
            case "vessel":
                String transMode = tab;
                Planning planning = planningRepository.findByOrderAndTransMode(order,
                        TransMode.valueOf(transMode.toUpperCase()).getType());
                if (null == planning) planning = new Planning();
                model.addAttribute("planning", planning);
            case "charge":
                model.addAttribute("charge", new Charge());
            case "summary":
            default:
                break;
        }
        return "planning/" + tab;
    }

    @RequestMapping(value = "/{tab}", method = RequestMethod.POST, params = "addPlanning")
    public String addPlanning(@PathVariable String taskId,
                              @PathVariable String tab,
                              @ModelAttribute("planning") Planning planning,
                              Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("tab: {}", tab);
        model.addAttribute("tab", tab);

        String transMode = tab;

        Order order = orderRepository.findOne(bpmService.getOrderIdByTaskId(taskId));
        planning.setOrder(order);
        planning.setTransMode(TransMode.valueOf(transMode.toUpperCase()).getType());
        planning.setStatus(PlanningStatus.ADDED.getValue());
        Planning newOne = planningRepository.save(planning);

        model.addAttribute("planning", newOne);
        return "redirect:/task/" + taskId + "/planning/" + transMode;
    }

    @RequestMapping(value = "/{tab}", method = RequestMethod.POST, params = "applyMulti")
    public String applyMulti(@PathVariable String taskId,
                             @PathVariable String tab,
                             @ModelAttribute Planning planning,
                             Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planning: {}", planning);
        model.addAttribute("tab", tab);

        String transMode = tab;
        planning.setTransMode(TransMode.valueOf(transMode.toUpperCase()).getType());
        planning.setSameRoute(false);
        planningService.editPlanning(taskId, planning);

        String redirect = "redirect:/task/" + taskId + "/planning/" + TransMode.valueOf(planning.getTransMode()).toString().toLowerCase();
        logger.debug("redirect: {}", redirect);
        return redirect;
    }

    @RequestMapping(value = "/{tab}", method = RequestMethod.POST, params = "applySame")
    public String applySame(@PathVariable String taskId,
                            @PathVariable String tab,
                            @ModelAttribute Planning planning,
                            Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planning: {}", planning);
        model.addAttribute("tab", tab);

        String transMode = tab;
        planning.setTransMode(TransMode.valueOf(transMode.toUpperCase()).getType());
        planning.setSameRoute(true);
        planningService.editPlanning(taskId, planning);

        String redirect = "redirect:/task/" + taskId + "/planning/" + TransMode.valueOf(planning.getTransMode()).toString().toLowerCase();
        logger.debug("redirect: {}", redirect);
        return redirect;
    }

    @RequestMapping(value = "/{tab}", method = RequestMethod.POST, params = "deletePlanning")
    public String deletePlanning(@PathVariable String taskId,
                                 @PathVariable String tab,
                                 Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        model.addAttribute("tab", tab);

        String transMode = tab;
        Planning planning = planningService.deletePlanning(taskId, transMode);

        String redirect = "redirect:/task/" + taskId + "/planning/" + transMode;
        logger.debug("redirect: {}", redirect);
        return redirect;
    }

    @RequestMapping(value = "/{transMode}/new", method = RequestMethod.GET)
    public String initCreationRoute(@PathVariable String taskId,
                                    @PathVariable String transMode,
                                    Model model, Principal principal) {
        logger.debug("transMode: {}", transMode);

        TransMode mode = TransMode.valueOf(transMode.toUpperCase());

        List<Location> portLocations = locationRepository.findByType(LocationType.PORT.getType());
        long id = getDefaultLocationId(portLocations);

        switch (mode) {
            case RAILWAY:
                TransRailway railway = new TransRailway();
                List<Location> stationLocs = locationRepository.findByType(LocationType.STATION.getType());
                id = getDefaultLocationId(stationLocs);

                railway.setDestination(String.valueOf(id));
                model.addAttribute("transRailway", railway);
                model.addAttribute("locations", stationLocs);
                break;
            case BARGE:
                TransBarge barge = new TransBarge();

                barge.setDestination(String.valueOf(id));
                model.addAttribute("transBarge", barge);
                model.addAttribute("locations", portLocations);
                model.addAttribute("sps", spRepository.findByType(SpType.BARGE.getType()));
                break;
            case VESSEL:
                TransVessel vessel = new TransVessel();

                vessel.setDestination(String.valueOf(id));
                model.addAttribute("transVessel", vessel);
                model.addAttribute("locations", locationRepository.findByType(LocationType.PORT.getType()));
                model.addAttribute("sps", spRepository.findByType(SpType.VESSEL.getType()));
                break;
            default:
                break;
        }

        return "planning/" + transMode + "_edit";
    }

    private long getDefaultLocationId(List<Location> locations) {
        int size = locations.size();
        long id = 0;
        if (size < 2) {
            id = locations.get(0).getId();
        } else {
            id = locations.get(1).getId();
        }
        return id;
    }

    @RequestMapping(value = "/railway/new", method = RequestMethod.POST)
    public String processCreationRailway(@PathVariable String taskId,
                                         @Valid @ModelAttribute TransRailway railway,
                                         BindingResult result, Model model, Principal principal) {
        logger.debug("railway: {}", railway);

        if (result.hasErrors()) {
            return "planning/railway_edit";
        } else {
            planningService.createRailway(taskId, railway);
            String redirect = "redirect:/task/" + taskId + "/planning/railway";
            return redirect;
        }
    }

    @RequestMapping(value = "/barge/new", method = RequestMethod.POST)
    public String processCreationBarge(@PathVariable String taskId,
                                       @Valid @ModelAttribute TransBarge barge,
                                       BindingResult result, Model model, Principal principal) {
        logger.debug("barge: {}", barge);

        if (result.hasErrors()) {
            return "planning/barge_edit";
        } else {
            planningService.createBarge(taskId, barge);
            String redirect = "redirect:/task/" + taskId + "/planning/barge";
            return redirect;
        }
    }

    @RequestMapping(value = "/vessel/new", method = RequestMethod.POST)
    public String processCreationVessel(@PathVariable String taskId,
                                        @Valid @ModelAttribute TransVessel vessel,
                                        BindingResult result, Model model, Principal principal) {
        logger.debug("vessel: {}", vessel);

        if (result.hasErrors()) {
            return "planning/vessel_edit";
        } else {
            planningService.createVessel(taskId, vessel);
            String redirect = "redirect:/task/" + taskId + "/planning/vessel";
            return redirect;
        }
    }

    @RequestMapping(value = "/{transModeKey}/{routeId}/edit", method = RequestMethod.GET)
    public String initEditRoute(@PathVariable String transModeKey,
                                @PathVariable long routeId,
                                Model model, Principal principal) {
        logger.debug("transModeKey: {}", transModeKey);
        logger.debug("routeId: {}", routeId);

        TransMode mode = TransMode.valueOf(transModeKey.toUpperCase());
        switch (mode) {
            case RAILWAY:
                TransRailway railway = transRailwayRepository.findOne(routeId);

                railway.setOrigination(String.valueOf(railway.getSrcLoc().getId()));
                railway.setDestination(String.valueOf(railway.getDstLoc().getId()));
                // NOTE: the attribute key have to align to class name.
                model.addAttribute("transRailway", railway);
                model.addAttribute("locations", locationRepository.findByType(LocationType.STATION.getType()));
                break;
            case BARGE:
                TransBarge barge = transBargeRepository.findOne(routeId);
                barge.setOrigination(String.valueOf(barge.getSrcLoc().getId()));
                barge.setDestination(String.valueOf(barge.getDstLoc().getId()));
                barge.setSpId(String.valueOf(barge.getSp().getId()));
                // NOTE: the attribute key have to align to class name.
                model.addAttribute("transBarge", barge);
                model.addAttribute("locations", locationRepository.findByType(LocationType.PORT.getType()));
                model.addAttribute("sps", spRepository.findByType(SpType.BARGE.getType()));
                break;
            case VESSEL:
                TransVessel vessel = transVesselRepository.findOne(routeId);
                vessel.setOrigination(String.valueOf(vessel.getSrcLoc().getId()));
                vessel.setDestination(String.valueOf(vessel.getDstLoc().getId()));
                vessel.setSpId(String.valueOf(vessel.getSp().getId()));
                // NOTE: the attribute key have to align to class name.
                model.addAttribute("transVessel", vessel);
                model.addAttribute("locations", locationRepository.findByType(LocationType.PORT.getType()));
                model.addAttribute("sps", spRepository.findByType(SpType.VESSEL.getType()));
                break;
            default:
                break;
        }

        return "planning/" + transModeKey + "_edit";
    }

    @RequestMapping(value = "/railway/{routeId}/edit", method = RequestMethod.PUT)
    public String processEditRailway(@PathVariable long routeId,
                                     @Valid @ModelAttribute TransRailway railway,
                                     BindingResult result, Model model, Principal principal) {
        logger.debug("routeId: {}", routeId);
        logger.debug("railway: {}", railway);

        if (result.hasErrors()) {
            return "planning/railway_edit";
        } else {
            TransRailway oldOne = planningService.editRailway(railway);
            String redirect = "redirect:/task/" + oldOne.getTaskId() + "/planning/railway";
            return redirect;
        }
    }

    @RequestMapping(value = "/barge/{routeId}/edit", method = RequestMethod.PUT)
    public String processEditBarge(@PathVariable long routeId,
                                   @Valid @ModelAttribute TransBarge barge,
                                   BindingResult result, Model model, Principal principal) {
        logger.debug("routeId: {}", routeId);
        logger.debug("barge: {}", barge);

        if (result.hasErrors()) {
            return "planning/barge_edit";
        } else {
            TransBarge oldOne = planningService.editBarge(barge);
            String redirect = "redirect:/task/" + oldOne.getTaskId() + "/planning/barge";
            return redirect;
        }
    }

    @RequestMapping(value = "/vessel/{routeId}/edit", method = RequestMethod.PUT)
    public String processEditVessel(@PathVariable long routeId,
                                    @Valid @ModelAttribute TransVessel vessel,
                                    BindingResult result, Model model, Principal principal) {
        logger.debug("routeId: {}", routeId);
        logger.debug("vessel: {}", vessel);

        if (result.hasErrors()) {
            return "planning/vessel_edit";
        } else {
            TransVessel oldOne = planningService.editVessel(vessel);
            String redirect = "redirect:/task/" + oldOne.getTaskId() + "/planning/vessel";
            return redirect;
        }
    }
}
