package liquid.order.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 2/11/15.
 */
public class ValueAddedOrder extends Order {
    private List<String> bicCodes = new ArrayList<String>();

    public List<String> getBicCodes() {
        return bicCodes;
    }

    public void setBicCodes(List<String> bicCodes) {
        this.bicCodes = bicCodes;
    }
}
