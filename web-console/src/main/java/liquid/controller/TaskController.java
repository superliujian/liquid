package liquid.controller;

import liquid.dto.TaskDto;
import liquid.service.NotCompletedException;
import liquid.service.PlanningService;
import liquid.service.TaskService;
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

import javax.servlet.http.HttpServletRequest;
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
public class TaskController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Deprecated
    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET)
    public String tasks(Model model, Principal principal) {
        logger.debug("Role: {}", RoleHelper.getRole(principal));
        TaskDto[] tasks = taskService.listTasks(RoleHelper.getRole(principal));
        model.addAttribute("tasks", tasks);
        model.addAttribute("title", "task.queue");
        //TODO: Using js to implement the function
        model.addAttribute("queueActive", "active");
        model.addAttribute("myActive", "");
        return "task/list";
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public String myTasks(Model model, Principal principal) {
        TaskDto[] tasks = taskService.listMyTasks(principal.getName());
        model.addAttribute("tasks", tasks);
        model.addAttribute("title", "task.my");
        //TODO: Using js to implement the function
        model.addAttribute("queueActive", "");
        model.addAttribute("myActive", "active");
        return "task/list";
    }

    /**
     * TODO: Move to another controller.
     *
     * @param taskId
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = "/{taskId}/common", method = RequestMethod.GET)
    public String toCommon(@PathVariable String taskId,
                           Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        TaskDto task = taskService.getTask(taskId);
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

        return "redirect:" + taskService.computeTaskMainPath(taskId);
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
                           @RequestHeader(value = "referer") String referer,
                           Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        try {
            taskService.complete(taskId, principal.getName());
        } catch (NotCompletedException e) {
            model.addAttribute("task_error", getMessage(e.getCode()));
            return "redirect:" + referer;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer;
        }

        return "redirect:/task";
    }
}
