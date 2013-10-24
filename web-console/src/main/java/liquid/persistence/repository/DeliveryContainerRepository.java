package liquid.persistence.repository;

import liquid.persistence.domain.BargeContainer;
import liquid.persistence.domain.DeliveryContainer;
import liquid.persistence.domain.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/24/13
 * Time: 10:57 PM
 */
public interface DeliveryContainerRepository extends CrudRepository<DeliveryContainer, Long> {
    Collection<DeliveryContainer> findByOrder(Order order);
}
