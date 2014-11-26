package liquid.transport.web.domain;

import liquid.transport.persistence.domain.ShipmentEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by redbrick9 on 9/12/14.
 */
public class Shipment {
    private Long id;
    private Integer containerQuantity;
    private RailTransport[] railTransport;

    public static Shipment valueOf(ShipmentEntity entity) {
        Shipment shipment = new Shipment();
        shipment.setId(entity.getId());
        shipment.setContainerQuantity(entity.getContainerQty());
        // TODO: BUG
        shipment.setRailTransport(RailTransport.valueOf(entity.getRailContainers()));
        return shipment;
    }

    public static Shipment[] valueOf(Iterable<ShipmentEntity> entities) {
        List<Shipment> shipments = new ArrayList<Shipment>();

        for (ShipmentEntity entity : entities) {
            shipments.add(valueOf(entity));
        }
        return shipments.toArray(new Shipment[0]);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getContainerQuantity() {
        return containerQuantity;
    }

    public void setContainerQuantity(Integer containerQuantity) {
        this.containerQuantity = containerQuantity;
    }

    public RailTransport[] getRailTransport() {
        return railTransport;
    }

    public void setRailTransport(RailTransport[] railTransport) {
        this.railTransport = railTransport;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Route{");
        sb.append("id=").append(id);
        sb.append(", containerQuantity=").append(containerQuantity);
        sb.append(", railTransport=").append(railTransport == null ? "null" : Arrays.asList(railTransport).toString());
        sb.append('}');
        return sb.toString();
    }
}
