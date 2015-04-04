package liquid.transport.web.domain;

/**
 * Created by redbrick9 on 8/26/14.
 */
public class Leg {
    private Long id;
    private Long shipmentId;
    private int transMode;
    private Long serviceProviderId;
    private Long sourceId;
    private String source;
    private Long destinationId;
    private String destination;
    private Boolean head;
    private Long prevId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public int getTransMode() {
        return transMode;
    }

    public void setTransMode(int transMode) {
        this.transMode = transMode;
    }

    public Long getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Long serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Boolean getHead() {
        return head;
    }

    public void setHead(Boolean head) {
        this.head = head;
    }

    public Long getPrevId() {
        return prevId;
    }

    public void setPrevId(Long prevId) {
        this.prevId = prevId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Leg");
        sb.append(", id=").append(id);
        sb.append(", shipmentId=").append(shipmentId);
        sb.append(", transMode=").append(transMode);
        sb.append(", serviceProviderId=").append(serviceProviderId);
        sb.append(", sourceId=").append(sourceId);
        sb.append(", source='").append(source).append('\'');
        sb.append(", destinationId=").append(destinationId);
        sb.append(", destination='").append(destination).append('\'');
        sb.append(", head=").append(head);
        sb.append(", prevId=").append(prevId);
        sb.append('}');
        return sb.toString();
    }
}
