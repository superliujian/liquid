package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 2:36 PM
 */
@Entity(name = "CONTAINER")
public class ContainerEntity extends BaseUpdateEntity {
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

    @Column(name = "STATUS")
    private int status;

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
}
