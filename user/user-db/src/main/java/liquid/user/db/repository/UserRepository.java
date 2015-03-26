package liquid.user.db.repository;

import liquid.user.domain.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Tao Ma on 3/24/15.
 */
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAll(Specification<User> specification);
}
