package liquid.persistence.domain;

import javax.persistence.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 11:53 AM
 */
@Entity(name = "PLANNING")
public class Planning {
    /**
     * {orderId} + - + {transMode}
     */
    @Id
    @Column(name = "ID")
    private String id;

    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "TRANS_MODE")
    @Deprecated
    private int transMode;

    @Column(name = "SAME_ROUTE")
    private boolean sameRoute;

    // 0, NULL; 1, ADDED; 2, FULL;
    @Column(name = "STATUS")
    private int status;

    @Transient
    private String transModeKey = "overview";

    public Planning() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getTransMode() {
        return transMode;
    }

    public void setTransMode(int transMode) {
        this.transMode = transMode;
    }

    public boolean isSameRoute() {
        return sameRoute;
    }

    public void setSameRoute(boolean sameRoute) {
        this.sameRoute = sameRoute;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void generateId() {
        this.id = order.getId() + "-" + transMode;
    }

    // Transient

    public String getTransModeKey() {
        return transModeKey;
    }

    public void setTransModeKey(String transModeKey) {
        this.transModeKey = transModeKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Planning{");
        sb.append("id='").append(id).append('\'');
        sb.append(", order=").append(order);
        sb.append(", transMode=").append(transMode);
        sb.append(", sameRoute=").append(sameRoute);
        sb.append(", status=").append(status);
        sb.append(", transModeKey='").append(transModeKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
