package liquid.process;

import org.activiti.engine.task.Task;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/3/13
 * Time: 12:08 PM
 */
public class TaskHelper {
    private TaskHelper() {
    }

    public static String stringOf(Task task) {
        if (null == task) {
            return "null";
        }
        StringBuilder sb = new StringBuilder("TaskDto{");
        sb.append("id='").append(task.getId()).append('\'');
        sb.append(", name='").append(task.getName()).append('\'');
        sb.append(", description='").append(task.getDescription()).append('\'');
        sb.append(", priority=").append(task.getDescription());
        sb.append(", owner='").append(task.getOwner()).append('\'');
        sb.append(", assignee='").append(task.getAssignee()).append('\'');
        sb.append(", delegationState=").append(task.getDelegationState());
        sb.append(", processInstanceId='").append(task.getProcessInstanceId()).append('\'');
        sb.append(", executionId='").append(task.getExecutionId()).append('\'');
        sb.append(", processDefinitionId='").append(task.getProcessDefinitionId()).append('\'');
        sb.append(", createTime=").append(task.getCreateTime());
        sb.append(", dueDate=").append(task.getDueDate());
        sb.append(", parentTaskId='").append(task.getParentTaskId()).append('\'');
        sb.append(", suspended=").append(task.isSuspended());
        sb.append('}');
        return sb.toString();
    }
}
