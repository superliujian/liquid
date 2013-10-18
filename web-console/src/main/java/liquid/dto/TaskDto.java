package liquid.dto;

import java.io.Serializable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/18/13
 * Time: 10:24 PM
 */
public class TaskDto implements Serializable {
    private String id;

    private long orderId;

    private String name;

    private String description;

    private String assignee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("TaskDto{");
        sb.append("id='").append(id).append('\'');
        sb.append(", orderId=").append(orderId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", assignee='").append(assignee).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
