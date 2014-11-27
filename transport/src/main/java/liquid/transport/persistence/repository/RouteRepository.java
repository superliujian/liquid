package liquid.transport.persistence.repository;

import liquid.transport.persistence.domain.RouteEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mat on 11/26/14.
 */
public interface RouteRepository extends CrudRepository<RouteEntity, Long> {
}
