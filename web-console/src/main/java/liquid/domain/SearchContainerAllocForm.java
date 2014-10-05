package liquid.domain;

/**
 * Created by mat on 10/5/14.
 */
public class SearchContainerAllocForm {
    private Long routeId;

    private Long containerId;

    private String bicCode;

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchContainerAllocForm{");
        sb.append("bicCode='").append(bicCode).append('\'');
        sb.append(", containerId=").append(containerId);
        sb.append(", routeId=").append(routeId);
        sb.append('}');
        return sb.toString();
    }
}
