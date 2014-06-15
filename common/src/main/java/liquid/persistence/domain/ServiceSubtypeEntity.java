package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by redbrick9 on 5/9/14.
 */
@Entity(name = "SERVICE_SUBTYPE")
public class ServiceSubtypeEntity extends BaseUpdateEntity {
    @NotNull
    @NotEmpty
    @Column(name = "CODE")
    private String code;

    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private int status;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "serviceSubtypes")
    private Set<ServiceProviderEntity> serviceProviders = new HashSet<>();

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<ServiceProviderEntity> getServiceProviders() {
        return serviceProviders;
    }

    public void setServiceProviders(Set<ServiceProviderEntity> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }
}
