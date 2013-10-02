package liquid.controller;

import liquid.persistence.domain.Planning;
import liquid.persistence.domain.TransMode;
import liquid.persistence.domain.TransRailway;
import liquid.persistence.repository.TransRailwayRepository;
import liquid.service.TaskService;
import liquid.service.bpm.ActivitiEngineService;
import liquid.utils.RoleHelper;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/26/13
 * Time: 11:25 PM
 */
@Controller
@RequestMapping("/task")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private ActivitiEngineService bpmEngineService;

    @Autowired
    private TransRailwayRepository transRailwayRepository;

    @Autowired
    private TaskService taskService;

    @ModelAttribute("transModes")
    public TransMode[] populateTransMedes() {
        return TransMode.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void tasks(Model model, Principal principal) {
        logger.debug("Role: {}", RoleHelper.getRole(principal));
        List<Task> tasks = bpmEngineService.listTasks(RoleHelper.getRole(principal));
        model.addAttribute("tasks", tasks);
        model.addAttribute("title", "task.queue");
        //TODO: Using js to implement the function
        model.addAttribute("queueActive", "active");
        model.addAttribute("myActive", "");
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public String myTasks(Model model, Principal principal) {
        List<Task> tasks = bpmEngineService.listMyTasks(principal.getName());
        model.addAttribute("tasks", tasks);
        model.addAttribute("title", "task.my");
        //TODO: Using js to implement the function
        model.addAttribute("queueActive", "");
        model.addAttribute("myActive", "active");
        return "task";
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public String task(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        Task task = bpmEngineService.getTask(taskId);
        logger.debug("taskDefinitionKey: {}", task.getTaskDefinitionKey());
        switch (task.getTaskDefinitionKey()) {
            case "planRoute":
                model.addAttribute("task", task);
                return "redirect:/task/" + taskId + "/planning/overview";
            default:
                break;
        }
        return "task";
    }

    @RequestMapping(method = RequestMethod.POST, params = "claim")
    public String claim(@RequestParam String taskId,
                        Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        bpmEngineService.claimTask(taskId, principal.getName());
        return "redirect:/task/" + taskId;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doAction(@RequestParam String taskId,
                           @RequestParam String action,
                           @RequestParam int transMode,
                           @RequestParam boolean sameRoute,
                           Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("action: {}", action);
        logger.debug("transMode: {}", transMode);

        switch (action) {
            case "addTransportation":
                taskService.addTransportation(taskId, principal.getName(), transMode, sameRoute);
                break;
            default:
                break;
        }

        return "redirect:/task/" + taskId;
    }
}
