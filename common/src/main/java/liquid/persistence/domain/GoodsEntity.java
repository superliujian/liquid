package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 3:27 PM
 */
@Entity(name = "GOODS")
public class GoodsEntity extends BaseUpdateEntity {
    @NotNull
    @NotEmpty
    @Column(name = "NAME", nullable = true)
    private String name;

    public GoodsEntity() { }

    public GoodsEntity(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Goods{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append('}');
        return sb.toString();
    }
}


