package liquid.domain;

import liquid.transport.persistence.domain.ShipmentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 5/20/14.
 */
public class ShipmentContainerAllocation {
    private int type;

    private Long shipmentId;

    private ShipmentEntity shipment;

    protected List<ContainerAllocation> containerAllocations = new ArrayList<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public ShipmentEntity getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentEntity shipment) {
        this.shipment = shipment;
    }

    public List<ContainerAllocation> getContainerAllocations() {
        return containerAllocations;
    }

    public void setContainerAllocations(List<ContainerAllocation> containerAllocations) {
        this.containerAllocations = containerAllocations;
    }
}
