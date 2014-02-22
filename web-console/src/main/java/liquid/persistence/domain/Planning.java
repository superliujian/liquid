package liquid.persistence.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
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

    @Column(name = "SAME_ROUTE")
    private boolean sameRoute;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "planning")
    private Collection<Route> routes;

    public Planning() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isSameRoute() {
        return sameRoute;
    }

    public void setSameRoute(boolean sameRoute) {
        this.sameRoute = sameRoute;
    }

    // Transient

    public Collection<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Collection<Route> routes) {
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
