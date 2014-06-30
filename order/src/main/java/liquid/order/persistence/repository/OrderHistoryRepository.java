package liquid.order.persistence.repository;

import liquid.persistence.domain.OrderHistory;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 11/12/13
 * Time: 11:11 PM
 */
public interface OrderHistoryRepository extends CrudRepository<OrderHistory, Long> {
}
