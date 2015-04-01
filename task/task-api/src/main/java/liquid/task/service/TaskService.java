package liquid.task.service;

import liquid.task.NotCompletedException;
import liquid.task.domain.Task;
import liquid.task.domain.TaskBar;
import org.activiti.engine.history.HistoricTaskInstance;

import java.util.List;

/**
 * Created by Tao Ma on 1/14/15.
 */
public interface TaskService {
    Task getTask(String taskId);

    void complete(String taskId) throws NotCompletedException;

    String computeTaskMainPath(String taskId);

    Long getOrderIdByTaskId(String taskId);

    Object getVariable(String taskId, String variableName);

    void setVariable(String taskId, String variableName, Object value);

    TaskBar calculateTaskBar(String candidateGid, String uid);

    Task[] listTasks(String candidateGid);

    Task[] listMyTasks(String uid);

    Task[] listWarningTasks();

    // TODO: use liquid task instead.
    List<org.activiti.engine.task.Task> listTasksByOrderId(Long orderId);

    // TODO: use liquid task instead.
    List<HistoricTaskInstance> listCompltedTasks(String businessKey);
}
