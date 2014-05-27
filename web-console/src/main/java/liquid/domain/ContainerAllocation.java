package liquid.domain;

import liquid.persistence.domain.Route;

/**
 * Created by redbrick9 on 5/20/14.
 */
public class ContainerAllocation {
    private int type;

    private Route[] routes;

    private RouteContainerAllocation[] routeContainerAllocations;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Route[] getRoutes() {
        return routes;
    }

    public void setRoutes(Route[] routes) {
        this.routes = routes;
    }

    public RouteContainerAllocation[] getRouteContainerAllocations() {
        return routeContainerAllocations;
    }

    public void setRouteContainerAllocations(RouteContainerAllocation[] routeContainerAllocations) {
        this.routeContainerAllocations = routeContainerAllocations;
    }
}
