package liquid.transport.persistence.repository;

import liquid.persistence.PageRepository;
import liquid.transport.persistence.domain.ShipmentEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/11/13
 * Time: 12:18 AM
 */
public interface ShipmentRepository extends CrudRepository<ShipmentEntity, Long>, PageRepository<ShipmentEntity> {
    Iterable<ShipmentEntity> findByOrderId(Long orderId);
}
