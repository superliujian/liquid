package liquid.user.db.repository;

import liquid.user.domain.GroupMember;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 3/18/15.
 */
public interface GroupMemberRepository extends CrudRepository<GroupMember, Integer> {
    GroupMember findByUsername(String username);
}
