package liquid.persistence.repository;

import liquid.persistence.domain.Container;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/3/13
 * Time: 3:50 PM
 */
public interface ContainerRepository extends CrudRepository<Container, Long> {
    Iterable<Container> findByStatus(int status);
}
