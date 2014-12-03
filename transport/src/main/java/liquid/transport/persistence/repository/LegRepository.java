package liquid.transport.persistence.repository;

import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/11/13
 * Time: 12:17 AM
 */
public interface LegRepository extends CrudRepository<LegEntity, Long> {
    List<LegEntity> findByShipmentAndTransMode(ShipmentEntity shipment, int transMode);
}
