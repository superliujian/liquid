package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 2:36 PM
 */
@Entity(name = "CONTAINER")
public class Container extends BaseEntity {

    @NotNull @NotEmpty
    @Column(name = "BIC_CODE")
    private String bicCode;

    @NotNull @NotEmpty
    @Column(name = "OWNER")
    private String owner;

    @Column(name = "TYPE")
    private int type;

    @ManyToOne
    @JoinColumn(name = "YARD")
    private Location yard;

    @Transient
    private long yardId;

    @Column(name = "STATUS")
    private int status;

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Location getYard() {
        return yard;
    }

    public void setYard(Location yard) {
        this.yard = yard;
    }

    public long getYardId() {
        return yardId;
    }

    public void setYardId(long yardId) {
        this.yardId = yardId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Container{");
        sb.append("bicCode='").append(bicCode).append('\'');
        sb.append(", owner='").append(owner).append('\'');
        sb.append(", type=").append(type);
        sb.append(", yard=").append(yard);
        sb.append(", yardId=").append(yardId);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
