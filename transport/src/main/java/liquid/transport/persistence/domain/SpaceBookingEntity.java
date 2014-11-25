package liquid.transport.persistence.domain;

import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.BaseUpdateEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by redbrick9 on 8/15/14.
 */
@Entity(name = "SHP_SPACE_BOOKING")
public class SpaceBookingEntity extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @OneToOne
    @JoinColumn(name = "LEG_ID")
    private LegEntity leg;

    @NotNull
    @NotEmpty
    @Column(name = "BOOKING_NO")
    private String bookingNo;

    @ManyToOne
    @JoinColumn(name = "SHIPOWNER")
    private ServiceProviderEntity shipowner;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public LegEntity getLeg() {
        return leg;
    }

    public void setLeg(LegEntity leg) {
        this.leg = leg;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public ServiceProviderEntity getShipowner() {
        return shipowner;
    }

    public void setShipowner(ServiceProviderEntity shipowner) {
        this.shipowner = shipowner;
    }
}
