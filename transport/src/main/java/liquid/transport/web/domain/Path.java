package liquid.transport.web.domain;

/**
 * Created by Tao Ma on 11/28/14.
 */
public class Path {
    private Long id;
    private Long routeId;
    private Long fromId;
    private String from;
    private Long toId;
    private String to;
    private Integer transportMode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Path");
        sb.append(", id=").append(id);
        sb.append(", routeId=").append(routeId);
        sb.append(", fromId=").append(fromId);
        sb.append(", from='").append(from).append('\'');
        sb.append(", toId=").append(toId);
        sb.append(", to='").append(to).append('\'');
        sb.append(", transportMode=").append(transportMode);
        sb.append('}');
        return sb.toString();
    }
}
