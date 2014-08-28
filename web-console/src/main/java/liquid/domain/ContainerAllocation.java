package liquid.domain;

import liquid.shipping.persistence.domain.RouteEntity;

/**
 * Created by redbrick9 on 5/20/14.
 */
public class ContainerAllocation {
    private int type;

    private Iterable<RouteEntity> routes;

    private RouteContainerAllocation[] routeContainerAllocations;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Iterable<RouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(Iterable<RouteEntity> routes) {
        this.routes = routes;
    }

    public RouteContainerAllocation[] getRouteContainerAllocations() {
        return routeContainerAllocations;
    }

    public void setRouteContainerAllocations(RouteContainerAllocation[] routeContainerAllocations) {
        this.routeContainerAllocations = routeContainerAllocations;
    }
}
