package liquid.transport.persistence.repository;

import liquid.transport.persistence.domain.TransportEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/11/13
 * Time: 12:18 AM
 */
public interface RouteRepository extends CrudRepository<TransportEntity, Long> {
    Iterable<TransportEntity> findByOrderId(Long orderId);
}
