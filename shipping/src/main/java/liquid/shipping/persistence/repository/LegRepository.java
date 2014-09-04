package liquid.shipping.persistence.repository;

import liquid.shipping.persistence.domain.LegEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/11/13
 * Time: 12:17 AM
 */
public interface LegRepository extends CrudRepository<LegEntity, Long> {
    List<LegEntity> findByRouteAndTransMode(RouteEntity route, int transMode);
}
