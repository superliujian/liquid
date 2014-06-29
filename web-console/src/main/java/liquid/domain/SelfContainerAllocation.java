package liquid.domain;

/**
 * Created by redbrick9 on 5/24/14.
 */
public class SelfContainerAllocation {
    private long routeId;

    private long[] containerIds;

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public long[] getContainerIds() {
        return containerIds;
    }

    public void setContainerIds(long[] containerIds) {
        this.containerIds = containerIds;
    }
}
