package liquid.controller;

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
public abstract class BaseTaskController {
    @Autowired
    protected ActivitiEngineService bpmService;

    @ModelAttribute("taskId")
    public String populateTaskId(@PathVariable String taskId) {
        return taskId;
    }

    @ModelAttribute("task")
    public Task populateTask(@PathVariable String taskId) {
        return bpmService.getTask(taskId);
    }
}
