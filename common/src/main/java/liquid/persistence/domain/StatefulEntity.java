package liquid.persistence.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by mat on 10/15/14.
 */
@MappedSuperclass
public class StatefulEntity extends BaseUpdateEntity {
    /**
     * enabled or disabled
     */
    @Column(name = "STATUS")
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
