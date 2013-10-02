package liquid.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 4:28 PM
 */
@Entity(name = "TRANS_BARGE")
public class TransBarge extends BaseTrans {

    @OneToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider sp;

    public ServiceProvider getSp() {
        return sp;
    }

    public void setSp(ServiceProvider sp) {
        this.sp = sp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("TransBarge{");
        sb.append("sp=").append(sp);
        sb.append('}');
        return sb.toString();
    }
}
