package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column(name = "NAME", nullable = true, unique = true)
    private String name;

    @Column(name = "STATUS")
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GoodsEntity{");
        sb.append("super=").append(super.toString());
        sb.append(", name='").append(name).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}


