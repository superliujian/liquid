package liquid.controller;

import liquid.persistence.domain.Order;
import liquid.persistence.domain.Planning;
import liquid.persistence.domain.TransMode;
import liquid.persistence.domain.TransRailway;
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
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/railway", method = RequestMethod.GET)
    public String railway(@PathVariable String taskId,
                          Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        Task task = bpmService.getTask(taskId);
        Order order = orderRepository.findOne(bpmService.getOrderIdByTaskId(taskId));
        Planning planning = planningRepository.findOne(order.getId() + "-" + TransMode.RAILWAY.getType());
        if (planning == null) {
            planning = new Planning();
        }

        model.addAttribute("task", task);
        model.addAttribute("planning", planning);
        return "planning/railway";
    }

    @RequestMapping(method = RequestMethod.POST, params = "create")
    public String create(@PathVariable String taskId,
                         @ModelAttribute Planning planning,
                         Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planning: {}", planning);

        planningService.create(taskId, principal.getName(), planning);

        String redirect = "redirect:/task/" + taskId + "/planning/" + TransMode.valueOf(planning.getTransMode()).toString().toLowerCase();
        logger.debug("redirect: {}", redirect);
        return redirect;
    }

    @RequestMapping(method = RequestMethod.POST, params = "update")
    public String update(@PathVariable String taskId,
                         @ModelAttribute Planning planning,
                         Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planning: {}", planning);
        Planning oldOne = planningRepository.findOne(planning.getId());
        oldOne.setSameRoute(planning.isSameRoute());
        planningRepository.save(oldOne);

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
}
