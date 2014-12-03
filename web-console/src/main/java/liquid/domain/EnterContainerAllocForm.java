package liquid.domain;

import liquid.container.domain.Containers;

/**
 * Created by mat on 10/5/14.
 */
public class EnterContainerAllocForm extends Containers {
    private Long shipmentId;

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EnterContainerAllocForm{");
        sb.append("shipmentId=").append(shipmentId);
        sb.append('}');
        return sb.toString();
    }
}
