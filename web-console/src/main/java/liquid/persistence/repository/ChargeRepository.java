package liquid.persistence.repository;

import liquid.persistence.domain.ChargeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 8:37 PM
 */
public interface ChargeRepository extends CrudRepository<ChargeEntity, Long> {
    List<ChargeEntity> findByTaskId(String taskId);

    Iterable<ChargeEntity> findByOrderId(long orderId);

    Iterable<ChargeEntity> findByOrderOrderNoLike(String orderNo);

    Iterable<ChargeEntity> findBySpNameLike(String cumtomerName);

    Iterable<ChargeEntity> findByLegId(long legId);

    Iterable<ChargeEntity> findByRouteId(long routeId);

    Iterable<ChargeEntity> findByOrderIdAndCreateRole(long orderId, String createRole);
}
