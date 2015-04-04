package liquid.transport.persistence.repository;

import liquid.order.domain.OrderEntity;
import liquid.transport.persistence.domain.RailContainerEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 10:23 AM
 */
public interface RailContainerRepository extends CrudRepository<RailContainerEntity, Long> {
    Collection<RailContainerEntity> findByOrder(OrderEntity order);

    Collection<RailContainerEntity> findByShipment(ShipmentEntity shipment);

    Collection<RailContainerEntity> findByShipmentId(Long shipmentId);

    Iterable<RailContainerEntity> findByReleasedAtGreaterThan(Date releasedAt);

    Page<RailContainerEntity> findAll(Pageable pageable);
}
