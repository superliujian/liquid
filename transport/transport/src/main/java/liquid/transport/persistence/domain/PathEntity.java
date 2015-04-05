package liquid.transport.persistence.domain;

import liquid.operation.domain.Location;
import liquid.persistence.domain.BaseIdEntity;

import javax.persistence.*;

/**
 * Created by mat on 11/26/14.
 */
@Entity(name = "TSP_PATH")
public class PathEntity extends BaseIdEntity {
    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    private RouteEntity route;

    @ManyToOne
    @JoinColumn(name = "FROM_ID")
    private Location from;

    @ManyToOne
    @JoinColumn(name = "TO_ID")
    private Location to;

    @Column(name = "TSP_MODE")
    private Integer transportMode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "NEXT_ID")
    private PathEntity next;

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public Integer getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
    }

    public PathEntity getNext() {
        return next;
    }

    public void setNext(PathEntity next) {
        this.next = next;
    }
}
