package liquid.shipping.web.domain;

/**
 * Created by redbrick9 on 8/26/14.
 */
public class Leg {
    private int transMode;
    private Long serviceProviderId;
    private Long sourceId;
    private String source;
    private Long destinationId;
    private String destination;

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Leg{");
        sb.append("transMode=").append(transMode);
        sb.append(", serviceProviderId=").append(serviceProviderId);
        sb.append(", sourceId=").append(sourceId);
        sb.append(", source='").append(source).append('\'');
        sb.append(", destinationId=").append(destinationId);
        sb.append(", destination='").append(destination).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
