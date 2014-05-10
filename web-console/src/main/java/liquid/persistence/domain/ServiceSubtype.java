package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Created by redbrick9 on 5/9/14.
 */
@Entity(name = "SERVICE_SUBTYPE")
public class ServiceSubtype extends BaseEntity {
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceSubtype{");
        sb.append("code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
