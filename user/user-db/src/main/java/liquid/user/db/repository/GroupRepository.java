package liquid.user.db.repository;

import liquid.user.domain.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by Tao Ma on 3/18/15.
 */
public interface GroupRepository extends CrudRepository<Group, Integer> {
    Collection<Group> findAll();
}
