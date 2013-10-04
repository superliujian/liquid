package liquid.persistence.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 11:53 AM
 */
@Entity(name = "PLANNING")
public class Planning extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "TRANS_MODE")
    private int transMode;

    @Column(name = "SAME_ROUTE")
    private boolean sameRoute;

    // 0, NULL; 1, ADDED; 2, FULL;
    @Column(name = "STATUS")
    private int status;

    @Transient
    private List<TransRailway> railways = new ArrayList<TransRailway>();

    public Planning() {}

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

    // Transient

    public List<TransRailway> getRailways() {
        return railways;
    }

    public void setRailways(List<TransRailway> railways) {
        this.railways = railways;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Planning{");
        sb.append("order=").append(order);
        sb.append(", transMode=").append(transMode);
        sb.append(", sameRoute=").append(sameRoute);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
