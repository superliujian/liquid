package liquid.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/10/13
 * Time: 8:15 PM
 */
@Entity(name = "SHIPPING_CONTAINER")
public class ShippingContainer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "CONTAINER_ID")
    private Container container;

    @Transient
    private long containerId;

    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    private Route route;

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public long getContainerId() {
        return containerId;
    }

    public void setContainerId(long containerId) {
        this.containerId = containerId;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ShippingContainer{");
        sb.append("container=").append(container);
        sb.append(", containerId=").append(containerId);
        sb.append(", route=").append(route);
        sb.append('}');
        return sb.toString();
    }
}
