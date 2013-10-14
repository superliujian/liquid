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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("BaseLegContainer{");
        sb.append("order=").append(order);
        sb.append(", leg=").append(leg);
        sb.append(", sc=").append(sc);
        sb.append('}');
        return sb.toString();
    }
}
