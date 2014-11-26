package liquid.domain;

import java.util.Arrays;

/**
 * Created by redbrick9 on 5/24/14.
 */
public class SelfContainerAllocation {
    private Long shipmentId;

    private Long[] containerIds;

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Long[] getContainerIds() {
        return containerIds;
    }

    public void setContainerIds(Long[] containerIds) {
        this.containerIds = containerIds;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SelfContainerAllocation{");
        sb.append("shipmentId=").append(shipmentId);
        sb.append(", containerIds=").append(Arrays.toString(containerIds));
        sb.append('}');
        return sb.toString();
    }
}
