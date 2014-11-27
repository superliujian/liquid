package liquid.transport.persistence.domain;

import liquid.persistence.domain.BaseIdEntity;
import liquid.persistence.domain.LocationEntity;

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
    private LocationEntity from;

    @ManyToOne
    @JoinColumn(name = "TO_ID")
    private LocationEntity to;

    @Column(name = "TSP_MODE")
    private Integer transportMode;

    @OneToOne
    @JoinColumn(name = "NEXT_ID")
    private PathEntity next;

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public LocationEntity getFrom() {
        return from;
    }

    public void setFrom(LocationEntity from) {
        this.from = from;
    }

    public LocationEntity getTo() {
        return to;
    }

    public void setTo(LocationEntity to) {
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
