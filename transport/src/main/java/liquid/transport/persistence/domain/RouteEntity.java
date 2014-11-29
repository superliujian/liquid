package liquid.transport.persistence.domain;

import liquid.persistence.domain.BaseStatusEntity;
import liquid.persistence.domain.LocationEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by mat on 11/26/14.
 */
@Entity(name = "TSP_ROUTE")
public class RouteEntity extends BaseStatusEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "COMMENT")
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "HEAD_ID")
    private PathEntity head;

    @ManyToOne
    @JoinColumn(name = "FROM_ID")
    private LocationEntity from;

    @ManyToOne
    @JoinColumn(name = "TO_ID")
    private LocationEntity to;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "route")
    private List<PathEntity> paths;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocationEntity getFrom() {
        return from;
    }

    public PathEntity getHead() {
        return head;
    }

    public void setHead(PathEntity head) {
        this.head = head;
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

    public List<PathEntity> getPaths() {
        return paths;
    }

    public void setPaths(List<PathEntity> paths) {
        this.paths = paths;
    }
}
