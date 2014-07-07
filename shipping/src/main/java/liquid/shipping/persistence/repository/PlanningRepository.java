package liquid.shipping.persistence.repository;

import liquid.order.persistence.domain.OrderEntity;
import liquid.shipping.persistence.domain.PlanningEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 12:07 PM
 */
public interface PlanningRepository extends CrudRepository<PlanningEntity, Long> {
    PlanningEntity findByOrder(OrderEntity order);
}
