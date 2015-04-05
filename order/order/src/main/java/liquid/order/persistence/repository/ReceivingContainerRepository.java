package liquid.order.persistence.repository;

import liquid.order.persistence.domain.ReceivingContainerEntity;
import liquid.order.domain.ReceivingOrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 6:34 PM
 */
public interface ReceivingContainerRepository extends CrudRepository<ReceivingContainerEntity, Long> {
    Collection<ReceivingContainerEntity> findByReceivingOrder(ReceivingOrderEntity recvOrder);
}
