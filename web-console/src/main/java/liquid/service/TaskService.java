package liquid.service;

import liquid.dto.TaskDto;
import liquid.service.bpm.ActivitiEngineService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
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

    public TaskDto getTask(String taskId) {
        Task task = bpmService.getTask(taskId);
        TaskDto taskDto = toTaskDto(task);

        long orderId = getOrderIdByTaskId(task.getId());
        taskDto.setOrderId(orderId);
        return taskDto;
    }

    public TaskDto[] listTasks(String candidateGid) {
        List<Task> list = bpmService.listTasks(candidateGid);
        return toTaskDtoArray(list);
    }

    public TaskDto[] listMyTasks(String uid) {
        List<Task> list = bpmService.listMyTasks(uid);
        return toTaskDtoArray(list);
    }

    public long getOrderIdByTaskId(String taskId) {
        String businessKey = bpmService.getBusinessKeyByTaskId(taskId);
        return null == businessKey ? 0L : Long.valueOf(businessKey);
    }

    public String computeTaskMainPath(String taskId) {
        Task task = bpmService.getTask(taskId);
        return computeTaskMainPath(task);
    }

    private String computeTaskMainPath(Task task) {
        switch (task.getTaskDefinitionKey()) {
            case "planRoute":
                return "/task/" + task.getId() + "/planning";
            case "allocateContainers":
                return "/task/" + task.getId() + "/allocation";
            case "loadOnYard":
                return "/task/" + task.getId() + "/rail";
            case "applyRailwayPlan":
                return "/task/" + task.getId() + "/rail_plan";
            case "loadByTruck":
                return "/task/" + task.getId() + "/rail_truck";
            case "recordTory":
                return "/task/" + task.getId() + "/rail_yard";
            case "recordTod":
                return "/task/" + task.getId() + "/rail_shipping";
            case "recordToa":
                return "/task/" + task.getId() + "/rail_arrival";
            case "doBargeOps":
                return "/task/" + task.getId() + "/barge";
            case "doVesselOps":
                return "/task/" + task.getId() + "/vessel";
            case "deliver":
                return "/task/" + task.getId() + "/delivery";
            case "planLoading":
            default:
                return "/task/" + task.getId() + "/common";
        }
    }

    private TaskDto[] toTaskDtoArray(List<Task> list) {
        TaskDto[] tasks = new TaskDto[list.size()];
        for (int i = 0; i < tasks.length; i++) {
            Task task = list.get(i);
            tasks[i] = toTaskDto(task);
            long orderId = getOrderIdByTaskId(task.getId());
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
