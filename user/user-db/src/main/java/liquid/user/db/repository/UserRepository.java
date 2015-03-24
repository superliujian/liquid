package liquid.user.db.repository;

import liquid.user.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 3/24/15.
 */
public interface UserRepository extends CrudRepository<User, String> {
}
