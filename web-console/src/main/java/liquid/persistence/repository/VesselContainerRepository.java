package liquid.persistence.repository;

import liquid.persistence.domain.Order;
import liquid.persistence.domain.VesselContainer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 4:27 PM
 */
public interface VesselContainerRepository extends CrudRepository<VesselContainer, Long> {
    Collection<VesselContainer> findByOrder(Order order);
}
