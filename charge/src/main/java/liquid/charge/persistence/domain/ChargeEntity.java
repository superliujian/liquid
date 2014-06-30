package liquid.charge.persistence.domain;

import liquid.persistence.domain.BaseUpdateEntity;
import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.shipping.persistence.domain.Leg;
import liquid.shipping.persistence.domain.RouteEntity;

import javax.persistence.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 7:43 PM
 */
@Entity(name = "CHARGE")
public class ChargeEntity extends BaseUpdateEntity {

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(name = "TASK_ID")
    private String taskId;

    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    private RouteEntity route;

    @ManyToOne
    @JoinColumn(name = "LEG_ID")
    private Leg leg;

    @ManyToOne
    @JoinColumn(name = "SERVICE_SUBTYPE_ID")
    private ServiceSubtypeEntity serviceSubtype;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProviderEntity sp;

    @Column(name = "WAY")
    private int way = 1;

    @Column(name = "UNIT_PRICE")
    private long unitPrice;

    @Column(name = "TOTAL_PRICE")
    private long totalPrice;

    @Column(name = "CURRENCY")
    private int currency;

    @Column(name = "CREATE_ROLE")
    private String createRole;

    @Column(name = "STATUS")
    private int status;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Leg getLeg() {
        return leg;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public ServiceSubtypeEntity getServiceSubtype() {
        return serviceSubtype;
    }

    public void setServiceSubtype(ServiceSubtypeEntity serviceSubtype) {
        this.serviceSubtype = serviceSubtype;
    }

    public ServiceProviderEntity getSp() {
        return sp;
    }

    public void setSp(ServiceProviderEntity sp) {
        this.sp = sp;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public String getCreateRole() {
        return createRole;
    }

    public void setCreateRole(String createRole) {
        this.createRole = createRole;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
