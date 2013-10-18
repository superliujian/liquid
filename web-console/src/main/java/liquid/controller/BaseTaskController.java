package liquid.controller;

import liquid.dto.TaskDto;
import liquid.service.TaskService;
import liquid.service.bpm.ActivitiEngineService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/4/13
 * Time: 5:42 PM
 */
@Component
public abstract class BaseTaskController extends BaseController {
    @Autowired
    protected TaskService taskService;

    @ModelAttribute("task")
    public TaskDto populateTask(@PathVariable String taskId) {
        return taskService.getTask(taskId);
    }
}
