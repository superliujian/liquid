package liquid.controller;

import liquid.persistence.domain.TransMode;
import liquid.service.PlanningService;
import liquid.service.bpm.ActivitiEngineService;
import liquid.service.bpm.TaskHelper;
import liquid.utils.RoleHelper;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ActivitiEngineService bpmService;

    @Autowired
    private PlanningService planningService;

    @ModelAttribute("transModes")
    public TransMode[] populateTransMedes() {
        return TransMode.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void tasks(Model model, Principal principal) {
        logger.debug("Role: {}", RoleHelper.getRole(principal));
        List<Task> tasks = bpmService.listTasks(RoleHelper.getRole(principal));
        model.addAttribute("tasks", tasks);
        model.addAttribute("title", "task.queue");
        //TODO: Using js to implement the function
        model.addAttribute("queueActive", "active");
        model.addAttribute("myActive", "");
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public String myTasks(Model model, Principal principal) {
        List<Task> tasks = bpmService.listMyTasks(principal.getName());
        model.addAttribute("tasks", tasks);
        model.addAttribute("title", "task.my");
        //TODO: Using js to implement the function
        model.addAttribute("queueActive", "");
        model.addAttribute("myActive", "active");
        return "task";
    }

    @RequestMapping(value = "/{taskId}/common", method = RequestMethod.GET)
    public String toCommon(@PathVariable String taskId,
                           Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        Task task = bpmService.getTask(taskId);
        logger.debug("task: {}", TaskHelper.stringOf(task));
        model.addAttribute("task", task);
        return "task/common";
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public String toRealTask(@PathVariable String taskId,
                             Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        Task task = bpmService.getTask(taskId);
        logger.debug("task: {}", TaskHelper.stringOf(task));
        model.addAttribute("task", task);

        return "redirect:" + bpmService.computeTaskMainPath(taskId);
    }

    @RequestMapping(method = RequestMethod.POST, params = "claim")
    public String claim(@RequestParam String taskId,
                        Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        bpmService.claimTask(taskId, principal.getName());
        return "redirect:/task/" + taskId;
    }

    @RequestMapping(method = RequestMethod.POST, params = "complete")
    public String complete(@RequestParam String taskId,
                           Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        Map<String, Object> variableMap = new HashMap<String, Object>();

        Task task = bpmService.getTask(taskId);
        switch (task.getTaskDefinitionKey()) {
            case "planRoute":
                Map<String, Object> transTypes = planningService.getTransTypes(taskId);
                variableMap.putAll(transTypes);
                break;
            default:
                break;
        }

        bpmService.complete(taskId, principal.getName(), variableMap);
        return "redirect:/task";
    }
}
