package liquid.order.persistence.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 4:08 PM
 */
@Entity(name = "ORD_RECV")
public class ReceivingOrderEntity extends BaseOrder {
    @Transient
    private List<String> bicCodes = new ArrayList<String>();

    public List<String> getBicCodes() {
        return bicCodes;
    }

    public void setBicCodes(List<String> bicCodes) {
        this.bicCodes = bicCodes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ReceivingOrder{");
        sb.append("bicCodes=").append(bicCodes);
        sb.append('}');
        return sb.toString();
    }
}
