package liquid.transport.persistence.repository;

import liquid.transport.persistence.domain.ShipmentEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/11/13
 * Time: 12:18 AM
 */
public interface TransportRepository extends CrudRepository<ShipmentEntity, Long> {
    Iterable<ShipmentEntity> findByOrderId(Long orderId);
}
