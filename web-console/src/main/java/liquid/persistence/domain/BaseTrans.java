package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 4:30 PM
 */
@MappedSuperclass
public class BaseTrans extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "PLANNING_ID")
    private Planning planning;

    @ManyToOne
    @JoinColumn(name = "CONTAINER_ID")
    private Container container;

    @Transient
    private long containerId;

    /**
     * TODO: It is used to return to planning page. This is not good way.
     */
    @Deprecated
    @Column(name = "TASK_ID")
    private String taskId;

    @NotNull @NotEmpty
    @Column(name = "ORIGINATION")
    private String origination;

    @NotNull @NotEmpty
    @Column(name = "DESTINATION")
    private String destination;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getOrigination() {
        return origination;
    }

    public void setOrigination(String origination) {
        this.origination = origination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    // Transient

    public long getContainerId() {
        return containerId;
    }

    public void setContainerId(long containerId) {
        this.containerId = containerId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("BaseTrans{");
        sb.append("order=").append(order);
        sb.append(", planning=").append(planning);
        sb.append(", container=").append(container);
        sb.append(", containerId=").append(containerId);
        sb.append(", taskId='").append(taskId).append('\'');
        sb.append(", origination='").append(origination).append('\'');
        sb.append(", destination='").append(destination).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
