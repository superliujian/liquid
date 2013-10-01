package liquid.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 2:36 PM
 */
@Entity(name = "CONTAINER")
public class Container extends BaseEntity {

    @Column(name = "CONTAINER_NO")
    private String containerNo;

    @Column(name = "TRANS_MODE")
    private int transMode;

    @Column(name = "TRANS_ID")
    private int transId;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public int getTransMode() {
        return transMode;
    }

    public void setTransMode(int transMode) {
        this.transMode = transMode;
    }

    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Container{");
        sb.append("containerNo='").append(containerNo).append('\'');
        sb.append(", transMode=").append(transMode);
        sb.append(", transId=").append(transId);
        sb.append('}');
        return sb.toString();
    }
}
