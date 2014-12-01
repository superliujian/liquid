package liquid.persistence.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by Tao Ma on 12/1/14.
 */
@MappedSuperclass
public class BaseTaskEntity extends BaseUpdateEntity {
    @Column(name = "TASK_ID")
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
