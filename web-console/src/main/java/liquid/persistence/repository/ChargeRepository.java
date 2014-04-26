package liquid.persistence.repository;

import liquid.persistence.domain.Charge;
import liquid.persistence.domain.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 8:37 PM
 */
public interface ChargeRepository extends CrudRepository<Charge, Long> {
    List<Charge> findByTaskId(String taskId);

    Iterable<Charge> findByOrderId(long orderId);

    Iterable<Charge> findByOrderOrderNoLike(String orderNo);

    Iterable<Charge> findBySpNameLike(String cumtomerName);

    Iterable<Charge> findByLegId(long legId);

    Iterable<Charge> findByRouteId(long routeId);

    Iterable<Charge> findByOrderIdAndCreateRole(long orderId, String createRole);
}
