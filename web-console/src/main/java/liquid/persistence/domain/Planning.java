package liquid.persistence.domain;

import javax.persistence.*;
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
    private OrderEntity order;

    @Column(name = "SAME_ROUTE")
    private boolean sameRoute;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "planning")
    private List<RouteEntity> routes;

    public Planning() {
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public boolean isSameRoute() {
        return sameRoute;
    }

    public void setSameRoute(boolean sameRoute) {
        this.sameRoute = sameRoute;
    }

    public List<RouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Planning{");
        sb.append("order=").append(order);
        sb.append(", sameRoute=").append(sameRoute);
        sb.append(", routes=").append(routes);
        sb.append('}');
        return sb.toString();
    }
}
