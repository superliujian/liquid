package liquid.persistence.repository;

import liquid.persistence.domain.Order;
import liquid.persistence.domain.RailContainer;
import liquid.persistence.domain.Route;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 10:23 AM
 */
public interface RailContainerRepository extends CrudRepository<RailContainer, Long> {
    Collection<RailContainer> findByOrder(Order order);

    Collection<RailContainer> findByRoute(Route route);
}
