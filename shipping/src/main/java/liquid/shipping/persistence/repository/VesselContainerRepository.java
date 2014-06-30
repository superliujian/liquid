package liquid.shipping.persistence.repository;

import liquid.order.persistence.domain.OrderEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.domain.VesselContainer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 4:27 PM
 */
public interface VesselContainerRepository extends CrudRepository<VesselContainer, Long> {
    Collection<VesselContainer> findByOrder(OrderEntity order);

    Collection<VesselContainer> findByRoute(RouteEntity route);
}
