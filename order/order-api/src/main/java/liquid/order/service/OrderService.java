package liquid.order.service;

import liquid.order.domain.OrderEntity;

/**
 * Created by Tao Ma on 4/4/15.
 */
public interface OrderService {
    OrderEntity complete(Long orderId);
}
