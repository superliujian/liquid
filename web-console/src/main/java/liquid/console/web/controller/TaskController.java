package liquid.console.web.controller;

import liquid.console.web.bpm.ActivitiEngineService;
import liquid.console.web.utils.RoleHelper;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public void tasks(Model model, Principal principal) {
    }
}
