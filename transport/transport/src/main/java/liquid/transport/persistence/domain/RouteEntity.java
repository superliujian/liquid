package liquid.transport.persistence.domain;

import liquid.persistence.domain.BaseStatusEntity;
import liquid.operation.domain.Location;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Tao Ma on 11/26/14.
 *
 * Status:
 * created      创建,
 * activated    激活,
 * enabled      启用,
 * disabled     禁用.
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
    private Location from;

    @ManyToOne
    @JoinColumn(name = "TO_ID")
    private Location to;

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

    public Location getFrom() {
        return from;
    }

    public PathEntity getHead() {
        return head;
    }

    public void setHead(PathEntity head) {
        this.head = head;
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

    public List<PathEntity> getPaths() {
        return paths;
    }

    public void setPaths(List<PathEntity> paths) {
        this.paths = paths;
    }
}
