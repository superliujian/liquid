package liquid.persistence.domain;

import liquid.metadata.ContainerStatus;
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
public class ContainerEntity extends BaseEntity {
    @NotNull
    @NotEmpty
    @Column(name = "BIC_CODE")
    private String bicCode;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private ServiceProviderEntity owner;

    @ManyToOne
    @JoinColumn(name = "SUBTYPE_ID")
    private ContainerSubtypeEntity subtype;

    @ManyToOne
    @JoinColumn(name = "YARD_ID")
    private LocationEntity yard;

    @Transient
    private long yardId;

    @Column(name = "STATUS")
    private int status;

    public ContainerEntity() { }

    public ContainerEntity(Long id) {
        super(id);
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public ServiceProviderEntity getOwner() {
        return owner;
    }

    public void setOwner(ServiceProviderEntity owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ContainerSubtypeEntity getSubtype() {
        return subtype;
    }

    public void setSubtype(ContainerSubtypeEntity subtype) {
        this.subtype = subtype;
    }

    public LocationEntity getYard() {
        return yard;
    }

    public void setYard(LocationEntity yard) {
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
        sb.append(", type=").append(subtype);
        sb.append(", yard=").append(yard);
        sb.append(", yardId=").append(yardId);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
