package liquid.shipping.web.domain;

/**
 * Created by redbrick9 on 8/16/14.
 */
public class BookingItem {
    private Long id;
    private Long legId;
    private String source;
    private String destination;
    private Integer containerQuantity;
    private Long shipownerId;
    private String shipownerName;
    private String bookingNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLegId() {
        return legId;
    }

    public void setLegId(Long legId) {
        this.legId = legId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getContainerQuantity() {
        return containerQuantity;
    }

    public void setContainerQuantity(Integer containerQuantity) {
        this.containerQuantity = containerQuantity;
    }

    public Long getShipownerId() {
        return shipownerId;
    }

    public void setShipownerId(Long shipownerId) {
        this.shipownerId = shipownerId;
    }

    public String getShipownerName() {
        return shipownerName;
    }

    public void setShipownerName(String shipownerName) {
        this.shipownerName = shipownerName;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BookingItem{");
        sb.append("id=").append(id);
        sb.append(", legId=").append(legId);
        sb.append(", source='").append(source).append('\'');
        sb.append(", destination='").append(destination).append('\'');
        sb.append(", containerQuantity=").append(containerQuantity);
        sb.append(", shipownerId=").append(shipownerId);
        sb.append(", shipownerName='").append(shipownerName).append('\'');
        sb.append(", bookingNo='").append(bookingNo).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
