package liquid.user.db.repository;

import liquid.user.domain.Authority;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 3/24/15.
 */
public interface AuthorityRepository extends CrudRepository<Authority, Integer> {
}
