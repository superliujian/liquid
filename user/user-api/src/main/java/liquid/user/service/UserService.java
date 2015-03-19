package liquid.user.service;

import liquid.user.domain.Group;
import liquid.user.domain.User;

import java.util.Collection;

/**
 * Created by Tao Ma on 3/19/15.
 */
public interface UserService {
    Group addGroup(Group group);

    Collection<Group> findGroups();

    void register(User user);

    User find(String uid);

    Collection<User> findAll();
}
