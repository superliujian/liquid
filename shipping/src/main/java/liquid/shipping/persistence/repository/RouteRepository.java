package liquid.shipping.persistence.repository;

import liquid.shipping.persistence.domain.PlanningEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/11/13
 * Time: 12:18 AM
 */
public interface RouteRepository extends CrudRepository<RouteEntity, Long> {
    Collection<RouteEntity> findByPlanning(PlanningEntity planning);
}
