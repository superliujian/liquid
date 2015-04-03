package liquid.operation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import liquid.persistence.domain.StatefulEntity;
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
@Entity(name = "OPS_SERVICE_PROVIDER")
public class ServiceProvider extends StatefulEntity {
    @NotNull
    @NotEmpty
    @Column(name = "CODE")
    private String code;

    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;

    @Column(name = "Q_NAME")
    private String queryName;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private ServiceProviderType type;

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

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "OPS_SERVICE_PROVIDER_SUBTYPE",
            joinColumns = @JoinColumn(name = "SERVICE_PROVIDER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "SERVICE_SUBTYPE_ID", referencedColumnName = "ID"))
    private Set<ServiceSubtype> subtypes = new HashSet<>();

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

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public ServiceProviderType getType() {
        return type;
    }

    public void setType(ServiceProviderType type) {
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

    public Set<ServiceSubtype> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(Set<ServiceSubtype> subtypes) {
        this.subtypes = subtypes;
    }
}
