package liquid.transport.persistence.repository;

import liquid.transport.persistence.domain.TruckEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 1/1/15.
 */
public interface TruckRepository extends CrudRepository<TruckEntity, Long> {
    Iterable<TruckEntity> findByShipmentId(Long shipmentId);
}
