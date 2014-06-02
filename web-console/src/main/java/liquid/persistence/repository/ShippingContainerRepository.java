package liquid.persistence.repository;

import liquid.persistence.domain.RouteEntity;
import liquid.persistence.domain.ShippingContainer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:42 AM
 */
public interface ShippingContainerRepository extends CrudRepository<ShippingContainer, Long> {
    List<ShippingContainer> findByRoute(RouteEntity route);
}
