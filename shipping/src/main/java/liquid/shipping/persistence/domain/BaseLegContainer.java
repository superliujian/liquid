package liquid.shipping.persistence.domain;

import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.BaseUpdateEntity;

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
public class BaseLegContainer extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    private RouteEntity route;

    @ManyToOne
    @JoinColumn(name = "LEG_ID")
    private LegEntity leg;

    @OneToOne
    @JoinColumn(name = "SC_ID")
    private ShippingContainerEntity sc;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public LegEntity getLeg() {
        return leg;
    }

    public void setLeg(LegEntity leg) {
        this.leg = leg;
    }

    public ShippingContainerEntity getSc() {
        return sc;
    }

    public void setSc(ShippingContainerEntity sc) {
        this.sc = sc;
    }
}
