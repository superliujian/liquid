package liquid.controller;

import liquid.security.SecurityContext;
import liquid.service.TaskService;
import liquid.task.NotCompletedException;
import liquid.task.domain.Task;
import liquid.task.domain.TaskBar;
import liquid.task.service.ActivitiEngineService;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by Tao Ma on 12/1/14.
 */
@Controller
@RequestMapping("/task")
public class TasksController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TasksController.class);

    @Autowired
    private TaskService taskService;

    @Deprecated
    @Autowired
    private ActivitiEngineService bpmService;

    @RequestMapping(method = RequestMethod.GET)
    public String tasks(Model model) {
        return "redirect:/task?q=all";
    }

    @RequestMapping(method = RequestMethod.GET, params = "q")
    public String list(@RequestParam("q") String tab, Model model) {
        Task[] tasks;

        switch (tab) {
            case "all":
                tasks = taskService.listTasks(SecurityContext.getInstance().getRole());
                break;
            case "my":
                tasks = taskService.listMyTasks(SecurityContext.getInstance().getUsername());
                break;
            case "warning":
                tasks = taskService.listWarningTasks();
                break;
            default:
                tasks = new Task[0];
                break;
        }
        TaskBar taskBar = taskService.calculateTaskBar(SecurityContext.getInstance().getRole(),
                SecurityContext.getInstance().getUsername());
        taskBar.setTitle("task." + tab);
        model.addAttribute("taskBar", taskBar);
        model.addAttribute("tab", tab);
        model.addAttribute("tasks", tasks);
        return "task/list";
    }

    @RequestMapping(method = RequestMethod.POST, params = "claim")
    public String claim(@RequestParam String taskId, HttpServletRequest request) {
        logger.debug("taskId: {}", taskId);
        try {
            bpmService.claimTask(taskId, SecurityContext.getInstance().getUsername());
        } catch (ActivitiTaskAlreadyClaimedException e) {
            request.getSession().setAttribute("message",
                    messageSource.getMessage("task.claimed.by.someone.else", new String[]{}, Locale.CHINA));
            return "error/error";
        }
        return "redirect:/task/" + taskId;
    }

    @RequestMapping(method = RequestMethod.POST, params = "complete")
    public String complete(@RequestParam String taskId,
                           @RequestHeader(value = "referer") String referer,
                           Model model) {
        logger.debug("taskId: {}", taskId);

        try {
            taskService.complete(taskId);
        } catch (NotCompletedException e) {
            model.addAttribute("task_error", getMessage(e.getCode()));
            return "redirect:" + referer;
        } catch (Exception e) {
            logger.error(String.format("Complete taskId '%s' and referer '%s'.", taskId, referer), e);
            return "redirect:" + referer;
        }

        return "redirect:/task";
    }
}
