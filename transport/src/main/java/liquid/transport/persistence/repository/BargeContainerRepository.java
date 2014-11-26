package liquid.transport.persistence.repository;

import liquid.order.persistence.domain.OrderEntity;
import liquid.transport.persistence.domain.BargeContainerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 2:48 PM
 */
public interface BargeContainerRepository extends CrudRepository<BargeContainerEntity, Long> {
    Collection<BargeContainerEntity> findByOrder(OrderEntity order);

    Collection<BargeContainerEntity> findByRouteId(Long routeId);
}
