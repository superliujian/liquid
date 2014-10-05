package liquid.domain;

import java.util.Arrays;

/**
 * Created by redbrick9 on 5/24/14.
 */
public class SelfContainerAllocation {
    private Long routeId;

    private Long[] containerIds;

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
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
        sb.append("routeId=").append(routeId);
        sb.append(", containerIds=").append(Arrays.toString(containerIds));
        sb.append('}');
        return sb.toString();
    }
}
