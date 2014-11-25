package liquid.transport.web.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mat on 10/6/14.
 */
public class RouteProfile {
    private List<Leg> legs = new ArrayList<>();

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }
}
