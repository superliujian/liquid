package liquid.order.service;

import liquid.order.domain.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 4/4/15.
 */
public interface OrderService {
    OrderEntity complete(Long orderId);

    OrderEntity find(Long id);

    OrderEntity save(OrderEntity entity);

    // FIXME - The following methods for the internal use.
    String computeOrderNo(String role, String serviceCode);

    Page<OrderEntity> findByCreateUser(String username, Pageable pageable);

    Page<OrderEntity> findAll(Pageable pageable);

    Page<OrderEntity> findAll(final String orderNo, final String customerName, final String username, final Pageable pageable);

    Page<OrderEntity> findByStatus(Integer status, Pageable pageable);

    Page<OrderEntity> findByCustomerId(Long customerId, String createdBy, Pageable pageable);

    Page<OrderEntity> findByOrderNoLike(String orderNo, Pageable pageable);
}
