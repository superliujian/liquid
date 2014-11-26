package liquid.transport.web.domain;

import liquid.transport.persistence.domain.ShipmentEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by redbrick9 on 9/12/14.
 */
public class Route {
    private Long id;
    private Integer containerQuantity;
    private RailTransport[] railTransport;

    public static Route valueOf(ShipmentEntity entity) {
        Route route = new Route();
        route.setId(entity.getId());
        route.setContainerQuantity(entity.getContainerQty());
        // TODO: BUG
        route.setRailTransport(RailTransport.valueOf(entity.getRailContainers()));
        return route;
    }

    public static Route[] valueOf(Iterable<ShipmentEntity> entities) {
        List<Route> routes = new ArrayList<Route>();

        for (ShipmentEntity entity : entities) {
            routes.add(valueOf(entity));
        }
        return routes.toArray(new Route[0]);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getContainerQuantity() {
        return containerQuantity;
    }

    public void setContainerQuantity(Integer containerQuantity) {
        this.containerQuantity = containerQuantity;
    }

    public RailTransport[] getRailTransport() {
        return railTransport;
    }

    public void setRailTransport(RailTransport[] railTransport) {
        this.railTransport = railTransport;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Route{");
        sb.append("id=").append(id);
        sb.append(", containerQuantity=").append(containerQuantity);
        sb.append(", railTransport=").append(railTransport == null ? "null" : Arrays.asList(railTransport).toString());
        sb.append('}');
        return sb.toString();
    }
}
