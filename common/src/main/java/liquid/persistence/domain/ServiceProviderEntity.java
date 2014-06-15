package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 4:38 PM
 */
@Entity(name = "SERVICE_PROVIDER")
public class ServiceProviderEntity extends BaseUpdateEntity {
    @NotNull
    @NotEmpty
    @Column(name = "CODE")
    private String code;

    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private SpTypeEnity type;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "POSTCODE")
    private String postcode;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "CELL")
    private String cell;

    /**
     * enabled or disabled
     */
    @Column(name = "STATUS")
    private int status;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "SERVICE_PROVIDER_SUBTYPE",
            joinColumns = @JoinColumn(name = "SERVICE_PROVIDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "SERVICE_SUBTYPE_ID"))
    private Set<ServiceSubtypeEntity> serviceSubtypes = new HashSet<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpTypeEnity getType() {
        return type;
    }

    public void setType(SpTypeEnity type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<ServiceSubtypeEntity> getServiceSubtypes() {
        return serviceSubtypes;
    }

    public void setServiceSubtypes(Set<ServiceSubtypeEntity> serviceSubtypes) {
        this.serviceSubtypes = serviceSubtypes;
    }
}
