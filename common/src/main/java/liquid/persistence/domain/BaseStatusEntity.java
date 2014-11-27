package liquid.persistence.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by mat on 11/26/14.
 */
@MappedSuperclass
public class BaseStatusEntity extends BaseUpdateEntity {
    @Column(name = "STATUS")
    private Integer status = 0;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
