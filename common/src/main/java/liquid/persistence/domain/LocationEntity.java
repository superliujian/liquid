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
@Entity(name = "LOCATION")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME", "TYPE"})})
public class LocationEntity extends BaseUpdateEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private int type;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LocationEntity{");
        sb.append("super=").append(super.toString());
        sb.append(", name='").append(name).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
