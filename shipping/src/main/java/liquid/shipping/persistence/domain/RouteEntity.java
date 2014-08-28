package liquid.shipping.persistence.domain;

import liquid.order.persistence.domain.OrderEntity;
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
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Min(1)
    @Column(name = "CONTAINER_QTY")
    private int containerQty;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    public Collection<LegEntity> legs;

    @Transient
    private int containerQtyMax;

    @Transient
    private String deliveryDateStr;

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

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
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

    public Collection<LegEntity> getLegs() {
        return legs;
    }

    public void setLegs(Collection<LegEntity> legs) {
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
}
