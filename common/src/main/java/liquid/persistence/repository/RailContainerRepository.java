package liquid.persistence.repository;

import liquid.persistence.domain.OrderEntity;
import liquid.persistence.domain.RailContainer;
import liquid.persistence.domain.RouteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 10:23 AM
 */
public interface RailContainerRepository extends CrudRepository<RailContainer, Long> {
    Collection<RailContainer> findByOrder(OrderEntity order);

    Collection<RailContainer> findByRoute(RouteEntity route);
}
