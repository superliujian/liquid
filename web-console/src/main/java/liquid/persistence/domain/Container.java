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

    @Column(name = "BIC_CODE")
    private String bicCode;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "STATUS")
    private int status;

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Container{");
        sb.append("bicCode='").append(bicCode).append('\'');
        sb.append(", owner='").append(owner).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
