package liquid.persistence.domain;

import liquid.validation.constraints.ContainerQtyMax;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/10/13
 * Time: 9:15 PM
 */
@Entity(name = "ROUTE")
public class Route extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "PLANNING_ID")
    private Planning planning;

    @Transient
    private int containerQtyMax;

    @Min(1)
    @Column(name = "CONTAINER_QTY")
    private int containerQty;

    @Column(name = "DELIVERY_ADDR")
    private String deliveryAddress;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELIVERY_DATE")
    private Date deliveryDate;

    @Transient
    private String deliveryDateStr;

    @Transient
    private Collection<Leg> legs;

    @Transient
    private Collection<ShippingContainer> containers;

    @Transient
    private boolean batch;

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public int getContainerQtyMax() {
        return containerQtyMax;
    }

    public void setContainerQtyMax(int containerQtyMax) {
        this.containerQtyMax = containerQtyMax;
    }

    public int getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(int containerQty) {
        this.containerQty = containerQty;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryDateStr() {
        return deliveryDateStr;
    }

    public void setDeliveryDateStr(String deliveryDateStr) {
        this.deliveryDateStr = deliveryDateStr;
    }

    public Collection<Leg> getLegs() {
        return legs;
    }

    public void setLegs(Collection<Leg> legs) {
        this.legs = legs;
    }

    public Collection<ShippingContainer> getContainers() {
        return containers;
    }

    public void setContainers(Collection<ShippingContainer> containers) {
        this.containers = containers;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Route{");
        sb.append("planning=").append(planning);
        sb.append(", containerQtyMax=").append(containerQtyMax);
        sb.append(", containerQty=").append(containerQty);
        sb.append(", deliveryAddress='").append(deliveryAddress).append('\'');
        sb.append(", deliveryDate=").append(deliveryDate);
        sb.append(", deliveryDateStr='").append(deliveryDateStr).append('\'');
        sb.append(", legs=").append(legs);
        sb.append(", containers=").append(containers);
        sb.append(", batch=").append(batch);
        sb.append('}');
        return sb.toString();
    }
}
