package liquid.shipping.persistence.repository;

import liquid.order.persistence.domain.OrderEntity;
import liquid.shipping.persistence.domain.DeliveryContainer;
import liquid.shipping.persistence.domain.RouteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/24/13
 * Time: 10:57 PM
 */
public interface DeliveryContainerRepository extends CrudRepository<DeliveryContainer, Long> {
    Collection<DeliveryContainer> findByOrder(OrderEntity order);

    Collection<DeliveryContainer> findByRoute(RouteEntity route);
}
