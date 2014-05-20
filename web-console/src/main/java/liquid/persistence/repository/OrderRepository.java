package liquid.persistence.repository;

import liquid.persistence.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 5:19 PM
 */
public interface OrderRepository extends BaseOrderRepository<Order, Long> {
    Page<Order> findByCreateUser(String uid, Pageable pageable);
}