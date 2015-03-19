package liquid.user.persistence.repository;

import liquid.user.persistence.domain.GroupMemberEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 3/18/15.
 */
public interface GroupMemberRepository extends CrudRepository<GroupMemberEntity, Long> {
}
