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
    private ChargeTypeRepository ctRepository;

    @Autowired
    private ChargeRepository chargeRepository;

    @ModelAttribute("chargeWays")
    public ChargeWay[] populateChargeWays() {
        return ChargeWay.values();
    }

    @ModelAttribute("cts")
    public Iterable<ChargeType> populateChargeTypes() {
        return ctRepository.findAll();
    }

    @ModelAttribute("charges")
    public Iterable<Charge> populateCharges(@PathVariable String taskId) {
        return chargeRepository.findByTaskId(taskId);
    }

    @ModelAttribute("railways")
    public Iterable<TransRailway> populateRailways(@PathVariable String taskId) {
        return transRailwayRepository.findByTaskId(taskId);
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
                return "planning/" + tab;
            case "charge":
                model.addAttribute("charge", new Charge());
                return "charge";
            case "summary":
            default:
                return "planning/" + tab;
        }
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
        TransRailway railway = new TransRailway();
        model.addAttribute("transRailway", railway);
        return "planning/" + transMode + "_edit";
    }

    @RequestMapping(value = "/{transMode}/new", method = RequestMethod.POST)
    public String processCreationRoute(@PathVariable String taskId,
                                       @PathVariable String transMode,
                                       @Valid @ModelAttribute TransRailway railway,
                                       BindingResult result, Model model, Principal principal) {
        logger.debug("transMode: {}", transMode);
        logger.debug("railway: {}", railway);

        if (result.hasErrors()) {
            return "planning/" + transMode + "_edit";
        } else {
            planningService.create(taskId, railway);
            String redirect = "redirect:/task/" + taskId + "/planning/" + transMode;
            return redirect;
        }
    }

    @RequestMapping(value = "/{transModeKey}/{routeId}/edit", method = RequestMethod.GET)
    public String initEditRoute(@PathVariable String transModeKey,
                                @PathVariable long routeId,
                                Model model, Principal principal) {
        logger.debug("transModeKey: {}", transModeKey);
        logger.debug("routeId: {}", routeId);
        TransRailway railway = transRailwayRepository.findOne(routeId);
        // NOTE: the attribute key have to align to class name.
        model.addAttribute("transRailway", railway);
        return "planning/" + transModeKey + "_edit";
    }

    @RequestMapping(value = "/{transModeKey}/{routeId}/edit", method = RequestMethod.PUT)
    public String processEditRoute(@PathVariable String transModeKey,
                                   @PathVariable long routeId,
                                   @Valid @ModelAttribute TransRailway railway,
                                   BindingResult result, Model model, Principal principal) {
        logger.debug("transModeKey: {}", transModeKey);
        logger.debug("routeId: {}", routeId);
        logger.debug("railway: {}", railway);

        if (result.hasErrors()) {
            return "planning/" + transModeKey + "_edit";
        } else {
            TransRailway oldOne = planningService.edit(railway);
            String redirect = "redirect:/task/" + oldOne.getTaskId() + "/planning/" + transModeKey;
            return redirect;
        }
    }
}
