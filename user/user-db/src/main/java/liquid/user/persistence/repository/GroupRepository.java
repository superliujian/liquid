package liquid.user.persistence.repository;

import liquid.user.persistence.domain.GroupEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 3/18/15.
 */
public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
}
