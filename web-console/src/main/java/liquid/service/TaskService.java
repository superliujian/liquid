package liquid.service;

import liquid.dto.TaskDto;
import liquid.service.bpm.ActivitiEngineService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/18/13
 * Time: 11:12 PM
 */
@Service
public class TaskService {

    @Autowired
    private ActivitiEngineService bpmService;

    public TaskDto[] listTasks(String candidateGid) {
        List<Task> list = bpmService.listTasks(candidateGid);
        return toTaskDtoArray(list);
    }

    public TaskDto[] listMyTasks(String uid) {
        List<Task> list = bpmService.listMyTasks(uid);
        return toTaskDtoArray(list);
    }

    private TaskDto[] toTaskDtoArray(List<Task> list) {
        TaskDto[] tasks = new TaskDto[list.size()];
        for (int i = 0; i < tasks.length; i++) {
            Task task = list.get(i);
            tasks[i] = toTaskDto(task);
            long orderId = bpmService.getOrderIdByTaskId(task.getId());
            tasks[i].setOrderId(orderId);
        }
        return tasks;
    }

    private TaskDto toTaskDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setAssignee(task.getAssignee());
        return dto;
    }
}
