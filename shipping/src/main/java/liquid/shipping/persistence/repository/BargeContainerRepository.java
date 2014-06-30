package liquid.shipping.persistence.repository;

import liquid.order.persistence.domain.OrderEntity;
import liquid.shipping.persistence.domain.BargeContainer;
import liquid.shipping.persistence.domain.RouteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 2:48 PM
 */
public interface BargeContainerRepository extends CrudRepository<BargeContainer, Long> {
    Collection<BargeContainer> findByOrder(OrderEntity order);

    Collection<BargeContainer> findByRoute(RouteEntity route);
}
