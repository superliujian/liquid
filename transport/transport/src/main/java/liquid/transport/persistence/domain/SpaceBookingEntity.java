package liquid.transport.persistence.domain;

import liquid.operation.domain.ServiceProvider;
import liquid.order.domain.OrderEntity;
import liquid.persistence.domain.BaseUpdateEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by redbrick9 on 8/15/14.
 */
@Entity(name = "TSP_SPACE_BOOKING")
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
    private ServiceProvider shipowner;

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

    public ServiceProvider getShipowner() {
        return shipowner;
    }

    public void setShipowner(ServiceProvider shipowner) {
        this.shipowner = shipowner;
    }
}
