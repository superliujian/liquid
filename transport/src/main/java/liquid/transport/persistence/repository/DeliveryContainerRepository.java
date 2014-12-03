package liquid.transport.persistence.repository;

import liquid.order.persistence.domain.OrderEntity;
import liquid.transport.persistence.domain.DeliveryContainerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/24/13
 * Time: 10:57 PM
 */
public interface DeliveryContainerRepository extends CrudRepository<DeliveryContainerEntity, Long> {
    Collection<DeliveryContainerEntity> findByOrder(OrderEntity order);
}
