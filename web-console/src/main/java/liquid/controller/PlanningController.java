package liquid.controller;

import liquid.persistence.domain.*;
import liquid.persistence.repository.OrderRepository;
import liquid.persistence.repository.PlanningRepository;
import liquid.persistence.repository.TransRailwayRepository;
import liquid.service.PlanningService;
import liquid.service.bpm.ActivitiEngineService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
public class PlanningController {
    private static final Logger logger = LoggerFactory.getLogger(PlanningController.class);

    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private PlanningService planningService;

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransRailwayRepository transRailwayRepository;

    @ModelAttribute("transModes")
    public TransMode[] populateTransMedes() {
        return TransMode.values();
    }

    @ModelAttribute("railways")
    public Iterable<TransRailway> populateRailways() {
        return transRailwayRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, params = "apply")
    public String create(@PathVariable String taskId,
                         @ModelAttribute Planning planning,
                         Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planning: {}", planning);

        planningService.apply(taskId, principal.getName(), planning);

        String redirect = "redirect:/task/" + taskId + "/planning/" + TransMode.valueOf(planning.getTransMode()).toString().toLowerCase();
        logger.debug("redirect: {}", redirect);
        return redirect;
    }

    @RequestMapping(method = RequestMethod.POST, params = "delete")
    public String delete(@PathVariable String taskId,
                         @RequestParam String id,
                         Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("id: {}", id);
        Planning planning = planningService.delete(id);

        String redirect = "redirect:/task/" + taskId + "/planning/" + TransMode.valueOf(planning.getTransMode()).toString().toLowerCase();
        logger.debug("redirect: {}", redirect);
        return redirect;
    }

    @RequestMapping(value = "/{transModeKey}", method = RequestMethod.GET)
    public String transModeTab(@PathVariable String taskId,
                               @PathVariable String transModeKey,
                               Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("transModeKey: {}", transModeKey);
        Task task = bpmService.getTask(taskId);
        Order order = orderRepository.findOne(bpmService.getOrderIdByTaskId(taskId));

        Planning planning = new Planning();
        if ("overview".equals(transModeKey)) {

        } else {
            planning = planningRepository.findOne(order.getId() + "-" + TransMode.valueOf(transModeKey.toUpperCase()).getType());
            if (planning == null) {
                planning = new Planning();
                planning.setOrder(order);
                planning.setSameRoute(true);
                planning.setTransMode(TransMode.valueOf(transModeKey.toUpperCase()).getType());
                planning.setStatus(PlanningStatus.ADDED.getValue());
                planning.generateId();
                planningRepository.save(planning);
            }
        }
        planning.setTransModeKey(transModeKey);

        model.addAttribute("task", task);
        model.addAttribute("planning", planning);
        return "planning/" + transModeKey;
    }

    @RequestMapping(value = "/{transModeKey}/new", method = RequestMethod.GET)
    public String initCreationRoute(@PathVariable String taskId,
                                    @PathVariable String transModeKey,
                                    Model model, Principal principal) {
        logger.debug("transModeKey: {}", transModeKey);

        TransRailway railway = new TransRailway();

        // NOTE: the attribute key have to align to class name.
        model.addAttribute("transRailway", railway);
        return "planning/" + transModeKey + "_edit";
    }

    @RequestMapping(value = "/{transModeKey}/new", method = RequestMethod.POST)
    public String processCreationRoute(@PathVariable String taskId,
                                       @PathVariable String transModeKey,
                                       @Valid @ModelAttribute TransRailway railway,
                                       BindingResult result, Model model, Principal principal) {
        logger.debug("transModeKey: {}", transModeKey);
        logger.debug("railway: {}", railway);

        if (result.hasErrors()) {
            return "planning/" + transModeKey + "_edit";
        } else {
            planningService.create(taskId, railway);
            String redirect = "redirect:/task/" + taskId + "/planning/" + transModeKey;
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
