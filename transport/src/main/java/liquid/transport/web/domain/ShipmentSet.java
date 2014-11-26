package liquid.transport.web.domain;

/**
 * Created by redbrick9 on 9/11/14.
 */
public class ShipmentSet {
    private Long orderId;

    private Shipment[] shipments;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Shipment[] getShipments() {
        return shipments;
    }

    public void setShipments(Shipment[] shipments) {
        this.shipments = shipments;
    }
}
