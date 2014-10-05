package liquid.domain;

import liquid.container.domain.Containers;

/**
 * Created by mat on 10/5/14.
 */
public class EnterContainerAllocForm extends Containers {
    private Long routeId;

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EnterContainerAllocForm{");
        sb.append("routeId=").append(routeId);
        sb.append('}');
        return sb.toString();
    }
}
