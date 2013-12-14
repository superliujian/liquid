package liquid.persistence.repository;

import liquid.persistence.domain.BargeContainer;
import liquid.persistence.domain.Order;
import liquid.persistence.domain.Route;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 2:48 PM
 */
public interface BargeContainerRepository extends CrudRepository<BargeContainer, Long> {
    Collection<BargeContainer> findByOrder(Order order);

    Collection<BargeContainer> findByRoute(Route route);
}
