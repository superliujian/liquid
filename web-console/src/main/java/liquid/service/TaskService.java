package liquid.service;

import liquid.dto.TaskBadgeDto;
import liquid.dto.TaskDto;
import liquid.persistence.domain.Order;
import liquid.persistence.domain.Planning;
import liquid.persistence.domain.Route;
import liquid.persistence.domain.ShippingContainer;
import liquid.service.bpm.ActivitiEngineService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private OrderService orderService;

    @Autowired
    private PlanningService planningService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ShippingContainerService scService;

    public TaskDto getTask(String taskId) {
        Task task = bpmService.getTask(taskId);
        TaskDto taskDto = toTaskDto(task);

        long orderId = getOrderIdByTaskId(task.getId());
        taskDto.setOrderId(orderId);
        return taskDto;
    }

    public List<HistoricTaskInstance> listCompltedTasks(long orderId) {
        return bpmService.listCompltedTasks(String.valueOf(orderId));
    }

    public TaskDto[] listTasks(String candidateGid) {
        List<Task> list = bpmService.listTasks(candidateGid);
        return toTaskDtoArray(list);
    }

    public TaskDto[] listMyTasks(String uid) {
        List<Task> list = bpmService.listMyTasks(uid);
        return toTaskDtoArray(list);
    }

    public TaskDto[] listWarningTasks() {
        List<Task> list = bpmService.listWarningTasks();
        return toTaskDtoArray(list);
    }

    public TaskBadgeDto calculateTaskBadge(String candidateGid, String uid) {
        TaskBadgeDto taskBadge = new TaskBadgeDto();
        TaskDto[] queue = listTasks(candidateGid);
        TaskDto[] myTasks = listMyTasks(uid);
        TaskDto[] warnings = listWarningTasks();
        taskBadge.setQueueSize(queue.length);
        taskBadge.setMyTasksQty(myTasks.length);
        taskBadge.setWarningQty(warnings.length);
        return taskBadge;
    }

    public long getOrderIdByTaskId(String taskId) {
        String businessKey = bpmService.getBusinessKeyByTaskId(taskId);
        return null == businessKey ? 0L : Long.valueOf(businessKey);
    }

    public void complete(String taskId, String uid) throws NotCompletedException {
        Map<String, Object> variableMap = new HashMap<String, Object>();
        Task task = bpmService.getTask(taskId);

        switch (task.getTaskDefinitionKey()) {
            case "planRoute":
                Map<String, Object> transTypes = planningService.getTransTypes(taskId);
                variableMap.putAll(transTypes);
                break;
            case "allocateContainers":
                long orderId = getOrderIdByTaskId(taskId);
                Order order = orderService.find(orderId);
                Planning planning = planningService.findByOrder(order);
                Collection<Route> routes = routeService.findByPlanning(planning);
                int allocatedContainerQty = 0;
                for (Route route : routes) {
                    Collection<ShippingContainer> scs = scService.findByRoute(route);
                    allocatedContainerQty += scs.size();
                }
                if (allocatedContainerQty != order.getContainerQty()) {
                    throw new NotCompletedException("container.allocation.is.not.completed");
                }
                break;
            default:
                break;
        }

        bpmService.complete(taskId, uid, variableMap);
    }

    public List<Task> listTasksByOrderId(long orderId) {
        return bpmService.listTasksByOrderId(orderId);
    }

    public String computeTaskMainPath(String taskId) {
        Task task = bpmService.getTask(taskId);
        return computeTaskMainPath(task);
    }

    private String computeTaskMainPath(Task task) {
        switch (task.getTaskDefinitionKey()) {
            case "feedDistyPrice":
                return "/task/" + task.getId() + "/disty";
            case "planRoute":
                return "/task/" + task.getId() + "/planning";
            case "allocateContainers":
                return "/task/" + task.getId() + "/allocation";
            case "applyRailwayPlan":
                return "/task/" + task.getId() + "/rail_plan";
            case "loadOnYard":
            case "loadByTruck":
                return "/task/" + task.getId() + "/rail_truck";
            case "sendLoadingByTruck":
                return "/task/" + task.getId() + "/rail_truck/sending";
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
            case "adjustPrice":
                return "/task/" + task.getId() + "/extra";
            case "checkCostByMarketing":
            case "checkCostByOperating":
            case "checkFromMarketing":
            case "checkFromOperating":
                return "/task/" + task.getId() + "/check_amount";
            case "confirmPurchaingSettlement":
                return "/task/" + task.getId() + "/settlement";
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

    public Object getVariable(String taskId, String variableName) {
        return bpmService.getVariable(taskId, variableName);
    }

    public void setVariable(String taskId, String variableName, Object value) {
        bpmService.setVariable(taskId, variableName, value);
    }
}
