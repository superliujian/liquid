package liquid.domain;

import liquid.persistence.domain.Route;

/**
 * Created by redbrick9 on 5/20/14.
 */
public class RouteContainerAllocation {
    private Route route;

    private String bicCode;

    public RouteContainerAllocation() {}

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }
}
