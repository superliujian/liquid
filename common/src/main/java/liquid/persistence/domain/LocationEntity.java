package liquid.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 10:33 AM
 */
@Entity(name = "OPS_LOCATION")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME", "TYPE"})})
public class LocationEntity extends BaseUpdateEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private int type;

    @Column(name = "Q_NAME")
    private String queryName;

    public LocationEntity() { }

    public LocationEntity(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }
}
