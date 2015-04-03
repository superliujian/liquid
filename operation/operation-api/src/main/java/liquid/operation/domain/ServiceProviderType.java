package liquid.operation.domain;

import liquid.converter.Text;
import liquid.persistence.domain.BaseUpdateEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/25/13
 * Time: 7:31 PM
 */
@Entity(name = "OPS_SERVICE_PROVIDER_TYPE")
public class ServiceProviderType extends BaseUpdateEntity implements Text {
    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toText() {
        return String.valueOf(id);
    }
}
