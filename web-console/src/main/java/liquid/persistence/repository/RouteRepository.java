package liquid.persistence.repository;

import liquid.persistence.domain.Planning;
import liquid.persistence.domain.Route;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/11/13
 * Time: 12:18 AM
 */
public interface RouteRepository extends CrudRepository<Route, Long> {
    Collection<Route> findByPlanning(Planning planning);
}
