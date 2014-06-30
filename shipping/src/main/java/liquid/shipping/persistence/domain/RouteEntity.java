package liquid.shipping.persistence.domain;

import liquid.persistence.domain.BaseUpdateEntity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/10/13
 * Time: 9:15 PM
 */
@Entity(name = "ROUTE")
public class RouteEntity extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "PLANNING_ID")
    private Planning planning;

    @Transient
    private int containerQtyMax;

    @Min(1)
    @Column(name = "CONTAINER_QTY")
    private int containerQty;

    @Transient
    private String deliveryDateStr;

    @Transient
    private Collection<Leg> legs;

    @Transient
    private Collection<ShippingContainerEntity> containers;

    @Transient
    private Collection<RailContainer> railContainers;

    @Transient
    private Collection<BargeContainer> bargeContainers;

    @Transient
    private Collection<VesselContainer> vesselContainers;

    @Transient
    private Collection<DeliveryContainer> deliveryContainers;

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

    public Collection<ShippingContainerEntity> getContainers() {
        return containers;
    }

    public void setContainers(Collection<ShippingContainerEntity> containers) {
        this.containers = containers;
    }

    public Collection<RailContainer> getRailContainers() {
        return railContainers;
    }

    public void setRailContainers(Collection<RailContainer> railContainers) {
        this.railContainers = railContainers;
    }

    public Collection<BargeContainer> getBargeContainers() {
        return bargeContainers;
    }

    public void setBargeContainers(Collection<BargeContainer> bargeContainers) {
        this.bargeContainers = bargeContainers;
    }

    public Collection<VesselContainer> getVesselContainers() {
        return vesselContainers;
    }

    public void setVesselContainers(Collection<VesselContainer> vesselContainers) {
        this.vesselContainers = vesselContainers;
    }

    public Collection<DeliveryContainer> getDeliveryContainers() {
        return deliveryContainers;
    }

    public void setDeliveryContainers(Collection<DeliveryContainer> deliveryContainers) {
        this.deliveryContainers = deliveryContainers;
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
//        sb.append("planning=").append(planning);
        sb.append(", containerQtyMax=").append(containerQtyMax);
        sb.append(", containerQty=").append(containerQty);
        sb.append(", deliveryDateStr='").append(deliveryDateStr).append('\'');
        sb.append(", legs=").append(legs);
        sb.append(", batch=").append(batch);
        sb.append('}');
        return sb.toString();
    }
}
