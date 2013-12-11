package liquid.persistence.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/14/13
 * Time: 8:10 PM
 */
@MappedSuperclass
public class BaseLegContainer extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "LEG_ID")
    private Leg leg;

    @OneToOne
    @JoinColumn(name = "SC_ID")
    private ShippingContainer sc;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Leg getLeg() {
        return leg;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }

    public ShippingContainer getSc() {
        return sc;
    }

    public void setSc(ShippingContainer sc) {
        this.sc = sc;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("BaseLegContainer{");
        sb.append("order=").append(order.getId());
        sb.append(", route=").append(route.getId());
        sb.append(", leg=").append(leg.getId());
        sb.append(", sc=").append(sc.getId());
        sb.append('}');
        return sb.toString();
    }
}
