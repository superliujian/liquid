package liquid.shipping.web.domain;

/**
 * Created by redbrick9 on 9/11/14.
 */
public class Routes {
    private Long orderId;

    private Route[] routes;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Route[] getRoutes() {
        return routes;
    }

    public void setRoutes(Route[] routes) {
        this.routes = routes;
    }
}
