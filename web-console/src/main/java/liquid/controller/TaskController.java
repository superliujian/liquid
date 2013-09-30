package liquid.controller;

import liquid.service.bpm.ActivitiEngineService;
import liquid.utils.RoleHelper;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @ModelAttribute("tasks")
    public List<Task> populateTasks(Principal principal) {
        logger.debug("Role: {}", RoleHelper.getRole(principal));
        return bpmEngineService.listTask(RoleHelper.getRole(principal));
    }

    @RequestMapping(method = RequestMethod.GET)
    public void tasks(Model model, Principal principal) {}

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public String task(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        Task task = bpmEngineService.getTask(taskId);
        logger.debug("taskDefinitionKey: {}", task.getTaskDefinitionKey());
        switch (task.getTaskDefinitionKey()) {
            case "planRoute":
                model.addAttribute("task", task);
                return "task/planning";
            default:
                break;
        }
        return "task";
    }

    @RequestMapping(method = RequestMethod.POST, params = "claim")
    public String claim(@RequestParam String taskId,
                        Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        return "redirect:/task/" + taskId;
    }
}
