package liquid.order.persistence.repository;

import liquid.order.persistence.domain.ReceivingOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 4:31 PM
 */
public interface ReceivingOrderRepository extends CrudRepository<ReceivingOrderEntity, Long>, JpaRepository<ReceivingOrderEntity, Long> {
    Iterable<ReceivingOrderEntity> findByOrderNoLike(String orderNo);
}