package liquid.transport.persistence.domain;

import liquid.order.domain.OrderEntity;
import liquid.persistence.domain.BaseTaskEntity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/10/13
 * Time: 9:15 PM
 */
@Entity(name = "TSP_SHIPMENT")
public class ShipmentEntity extends BaseTaskEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Min(1)
    @Column(name = "CONTAINER_QTY")
    private int containerQty;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "shipment", orphanRemoval = true)
    public List<LegEntity> legs;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shipment")
    private List<ShippingContainerEntity> containers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shipment")
    private Collection<RailContainerEntity> railContainers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shipment")
    private Collection<BargeContainerEntity> bargeContainers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shipment")
    private Collection<VesselContainerEntity> vesselContainers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shipment")
    private Collection<DeliveryContainerEntity> deliveryContainers;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public int getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(int containerQty) {
        this.containerQty = containerQty;
    }

    public List<LegEntity> getLegs() {
        return legs;
    }

    public void setLegs(List<LegEntity> legs) {
        this.legs = legs;
    }

    public List<ShippingContainerEntity> getContainers() {
        return containers;
    }

    public void setContainers(List<ShippingContainerEntity> containers) {
        this.containers = containers;
    }

    public Collection<RailContainerEntity> getRailContainers() {
        return railContainers;
    }

    public void setRailContainers(Collection<RailContainerEntity> railContainers) {
        this.railContainers = railContainers;
    }

    public Collection<BargeContainerEntity> getBargeContainers() {
        return bargeContainers;
    }

    public void setBargeContainers(Collection<BargeContainerEntity> bargeContainers) {
        this.bargeContainers = bargeContainers;
    }

    public Collection<VesselContainerEntity> getVesselContainers() {
        return vesselContainers;
    }

    public void setVesselContainers(Collection<VesselContainerEntity> vesselContainers) {
        this.vesselContainers = vesselContainers;
    }

    public Collection<DeliveryContainerEntity> getDeliveryContainers() {
        return deliveryContainers;
    }

    public void setDeliveryContainers(Collection<DeliveryContainerEntity> deliveryContainers) {
        this.deliveryContainers = deliveryContainers;
    }
}
