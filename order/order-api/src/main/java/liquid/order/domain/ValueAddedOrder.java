package liquid.order.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 2/11/15.
 */
public class ValueAddedOrder extends BaseOrder {
    private List<TransportedContainer> containers = new ArrayList<TransportedContainer>();

    public List<TransportedContainer> getContainers() {
        return containers;
    }

    public void setContainers(List<TransportedContainer> containers) {
        this.containers = containers;
    }
}
