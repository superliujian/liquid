package liquid.service;

import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.task.AbstractTaskProxy;
import liquid.task.NotCompletedException;
import liquid.task.TaskFactory;
import liquid.task.domain.TaskBar;
import liquid.task.service.ActivitiEngineService;
import liquid.util.DatePattern;
import liquid.util.DateUtil;
import liquid.util.StringUtil;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/18/13
 * Time: 11:12 PM
 */
@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private OrderService orderService;

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected TaskFactory taskFactory;

    public liquid.task.domain.Task getTask(String taskId) {
        Task task = bpmService.getTask(taskId);
        liquid.task.domain.Task taskDto = toTaskDto(task);

        long orderId = getOrderIdByTaskId(task.getId());
        OrderEntity order = orderService.find(orderId);

        taskDto.setOrderId(orderId);
        taskDto.setOrderNo(order.getOrderNo());
        String prompt = messageSource.getMessage("task.complete.prompt." + task.getTaskDefinitionKey(), new Object[0], "", Locale.CHINA);
        if (!StringUtil.valid(prompt))
            prompt = messageSource.getMessage("task.complete.prompt.default", new Object[0], "", Locale.CHINA);
        taskDto.setPrompt(prompt);
        return taskDto;
    }

    public OrderEntity findOrderByTaskId(String taskId) {
        long orderId = getOrderIdByTaskId(taskId);
        return orderService.find(orderId);
    }

    public List<HistoricTaskInstance> listCompltedTasks(long orderId) {
        return bpmService.listCompltedTasks(String.valueOf(orderId));
    }

    public liquid.task.domain.Task[] listTasks(String candidateGid) {
        List<Task> list = bpmService.listTasks(candidateGid);
        return toTaskDtoArray(list);
    }

    public liquid.task.domain.Task[] listMyTasks(String uid) {
        List<Task> list = bpmService.listMyTasks(uid);
        return toTaskDtoArray(list);
    }

    public liquid.task.domain.Task[] listWarningTasks() {
        List<Task> list = bpmService.listWarningTasks();
        return toTaskDtoArray(list);
    }

    public TaskBar calculateTaskBar(String candidateGid, String uid) {
        TaskBar taskBadge = new TaskBar();
        liquid.task.domain.Task[] queue = listTasks(candidateGid);
        liquid.task.domain.Task[] myTasks = listMyTasks(uid);
        liquid.task.domain.Task[] warnings = listWarningTasks();
        taskBadge.setTaskQueueSize(queue.length);
        taskBadge.setMyTasksSize(myTasks.length);
        taskBadge.setWarningsSize(warnings.length);
        return taskBadge;
    }

    public Long getOrderIdByTaskId(String taskId) {
        String businessKey = bpmService.getBusinessKeyByTaskId(taskId);
        return null == businessKey ? null : Long.valueOf(businessKey);
    }

    public void complete(String taskId) throws NotCompletedException {
        Task activitiTask = bpmService.getTask(taskId);
        if (null == activitiTask) {
            logger.warn("The taskId '{}' is null.", taskId);
            return;
        }
        AbstractTaskProxy task = taskFactory.findTask(activitiTask.getTaskDefinitionKey());
        task.complete(taskId);
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
                return "/dp?t=" + task.getId();
            case "planRoute":
                return "/task/" + task.getId() + "/planning";
            case "allocateContainers":
            case "feedContainerNo":
                return "/task/" + task.getId() + "/allocation";
            case "applyRailwayPlan":
                return "/task/" + task.getId() + "/rail_plan";
            case "loadOnYard":
            case "loadByTruck":
            case "sendTruck":
            case "salesSendingTruck":
            case "marketingSendingTruck":
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
                return "/task/" + task.getId() + "/ajustement";
            case "checkCostByMarketing":
            case "checkCostByOperating":
            case "checkFromMarketing":
            case "checkFromOperating":
                return "/task/" + task.getId() + "/check_amount";
            case "confirmPurchaingSettlement":
                return "/task/" + task.getId() + "/settlement";
            case "bookingShippingSpace":
                return "/task/" + task.getId() + "/booking";
            default:
                return "/task/" + task.getId() + "/common";
        }
    }

    private liquid.task.domain.Task[] toTaskDtoArray(List<Task> list) {
        liquid.task.domain.Task[] tasks = new liquid.task.domain.Task[list.size()];
        for (int i = 0; i < tasks.length; i++) {
            Task task = list.get(i);
            tasks[i] = toTaskDto(task);
            long orderId = getOrderIdByTaskId(task.getId());
            OrderEntity order = orderService.find(orderId);
            tasks[i].setOrderId(orderId);
            tasks[i].setOrderNo(order.getOrderNo());
        }
        return tasks;
    }

    private liquid.task.domain.Task toTaskDto(Task task) {
        liquid.task.domain.Task dto = new liquid.task.domain.Task();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setAssignee(task.getAssignee());
        dto.setCreateDate(DateUtil.stringOf(task.getCreateTime(), DatePattern.UNTIL_SECOND));
        return dto;
    }

    public Object getVariable(String taskId, String variableName) {
        return bpmService.getVariable(taskId, variableName);
    }

    public void setVariable(String taskId, String variableName, Object value) {
        bpmService.setVariable(taskId, variableName, value);
    }
}
