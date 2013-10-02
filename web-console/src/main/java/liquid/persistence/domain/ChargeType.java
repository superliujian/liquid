package liquid.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 7:44 PM
 */
@Entity(name = "CHARGE_TYPE")
public class ChargeType extends BaseEntity {
    @Column(name = "NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ChargeType{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
