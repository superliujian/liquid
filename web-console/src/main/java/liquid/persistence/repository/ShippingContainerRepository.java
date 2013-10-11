package liquid.persistence.repository;

import liquid.persistence.domain.Route;
import liquid.persistence.domain.ShippingContainer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:42 AM
 */
public interface ShippingContainerRepository extends CrudRepository<ShippingContainer, Long> {
    Collection<ShippingContainer> findByRoute(Route route);
}
