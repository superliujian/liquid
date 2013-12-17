package liquid.persistence.domain;

import javax.persistence.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 7:43 PM
 */
@Entity(name = "CHARGE")
public class Charge extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "TASK_ID")
    private String taskId;

    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    private Route route;

    @Transient
    private long formRouteId;

    @ManyToOne
    @JoinColumn(name = "LEG_ID")
    private Leg leg;

    @Transient
    private long formLegId;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private ChargeType type;

    @Transient
    private long typeId;

    @Transient
    private long spId;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider sp;

    @Column(name = "WAY")
    private int way = 1;

    @Column(name = "UNIT_PRICE")
    private long unitPrice;

    @Column(name = "TOTAL_PRICE")
    private long totalPrice;

    @Column(name = "CREATE_ROLE")
    private String createRole;

    @Column(name = "STATUS")
    private int status;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
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

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public long getFormRouteId() {
        return formRouteId;
    }

    public void setFormRouteId(long formRouteId) {
        this.formRouteId = formRouteId;
    }

    public long getFormLegId() {
        return formLegId;
    }

    public void setFormLegId(long formLegId) {
        this.formLegId = formLegId;
    }

    public ChargeType getType() {
        return type;
    }

    public void setType(ChargeType type) {
        this.type = type;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getSpId() {
        return spId;
    }

    public void setSpId(long spId) {
        this.spId = spId;
    }

    public ServiceProvider getSp() {
        return sp;
    }

    public void setSp(ServiceProvider sp) {
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Charge{");
        sb.append("order=").append(order);
        sb.append(", taskId='").append(taskId).append('\'');
        sb.append(", formRouteId=").append(formRouteId);
        sb.append(", formLegId=").append(formLegId);
        sb.append(", type=").append(type);
        sb.append(", typeId=").append(typeId);
        sb.append(", spId=").append(spId);
        sb.append(", sp=").append(sp);
        sb.append(", way=").append(way);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", createRole='").append(createRole).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
