package liquid.operation.domain;

import liquid.persistence.domain.BaseUpdateEntity;

import javax.persistence.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 10:33 AM
 */
@Entity(name = "OPS_LOCATION")
public class Location extends BaseUpdateEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE_ID")
    private LocationType type;

    @Column(name = "Q_NAME")
    private String queryName;

    public Location() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }
}
