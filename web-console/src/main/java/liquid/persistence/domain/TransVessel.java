package liquid.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 5:00 PM
 */
@Entity(name = "TRANS_VESSEL")
public class TransVessel extends BaseTrans {
    @Transient
    private String spId;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider sp;

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public ServiceProvider getSp() {
        return sp;
    }

    public void setSp(ServiceProvider sp) {
        this.sp = sp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("TransVessel{");
        sb.append("spId='").append(spId).append('\'');
        sb.append(", sp=").append(sp);
        sb.append('}');
        return sb.toString();
    }
}
