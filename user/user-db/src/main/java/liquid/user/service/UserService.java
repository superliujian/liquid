package liquid.user.service;

import liquid.user.persistence.domain.GroupEntity;
import liquid.user.persistence.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Tao Ma on 3/18/15.
 */
@Service
public class UserService {

    @Autowired
    private GroupRepository repository;

    @Transactional
    public GroupEntity find(Long id) {
        GroupEntity group = repository.findOne(id);
        if (null != group) {
            group.getMembers().size();
        }
        return group;
    }

    public GroupEntity save(GroupEntity group) {
        return repository.save(group);
    }
}
