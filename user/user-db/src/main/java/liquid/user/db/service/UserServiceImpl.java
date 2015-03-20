package liquid.user.db.service;

import liquid.user.db.repository.GroupRepository;
import liquid.user.domain.Group;
import liquid.user.model.PasswordChange;
import liquid.user.model.User;
import liquid.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by Tao Ma on 3/19/15.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private GroupRepository repository;

    @Transactional
    public Group find(Integer id) {
        Group group = repository.findOne(id);
        if (null != group) {
            group.getMembers().size();
        }
        return group;
    }

    public Group save(Group group) {

        return repository.save(group);
    }

    @Override
    public Group addGroup(Group group) {
        return null;
    }

    @Override
    public Collection<Group> findGroups() {
        return null;
    }

    @Override
    public void register(User user) {

    }

    @Override
    public boolean authenticate(String userDn, String credentials) {
        return false;
    }

    @Override
    public User find(String uid) {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        return null;
    }

    @Override
    public void lock(String uid) {

    }

    @Override
    public void unlock(String uid) {

    }

    @Override
    public void edit(User user) {

    }

    @Override
    public void resetPassword(String uid, PasswordChange passwordChange) {

    }
}
