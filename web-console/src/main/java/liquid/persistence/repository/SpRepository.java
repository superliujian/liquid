package liquid.persistence.repository;

import liquid.persistence.domain.ServiceProvider;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 5:03 PM
 */
public interface SpRepository extends CrudRepository<ServiceProvider, Long> {
    Iterable<ServiceProvider> findByTypeId(long type);
}
