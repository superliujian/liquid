package liquid.shipping.web.domain;

import liquid.shipping.persistence.domain.RouteEntity;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by redbrick9 on 9/11/14.
 */
public class Routes {
    private Long orderId;

    private RailTransport[] railTransport;

    public Collection<RouteEntity> toEntities() {
        return Collections.emptyList();
    }

    public static void valueOf(Collection<RouteEntity> entities) {
        
    }
}
