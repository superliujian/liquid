package liquid.persistence.domain;

import javax.persistence.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 7:43 PM
 */
@Entity(name = "CHARGE")
public class Charge extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "TASK_ID")
    private String taskId;

    @Column(name = "TYPE")
    private long type;

    @Column(name = "WAY")
    private int way;

    @Column(name = "UNIT_PRICE")
    private long unitPrice;

    @Column(name = "TOTAL_PRICE")
    private long totalPrice;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Charge{");
        sb.append("order=").append(order);
        sb.append(", taskId='").append(taskId).append('\'');
        sb.append(", type=").append(type);
        sb.append(", way=").append(way);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append('}');
        return sb.toString();
    }
}
