package liquid.domain;

import liquid.transport.persistence.domain.TransportEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 5/20/14.
 */
public class RouteContainerAllocation {
    private int type;

    private Long routeId;

    private TransportEntity route;

    protected List<ContainerAllocation> containerAllocations = new ArrayList<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public TransportEntity getRoute() {
        return route;
    }

    public void setRoute(TransportEntity route) {
        this.route = route;
    }

    public List<ContainerAllocation> getContainerAllocations() {
        return containerAllocations;
    }

    public void setContainerAllocations(List<ContainerAllocation> containerAllocations) {
        this.containerAllocations = containerAllocations;
    }
}
