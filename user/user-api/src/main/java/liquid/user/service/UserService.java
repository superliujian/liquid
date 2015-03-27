package liquid.user.service;

import liquid.user.domain.Group;
import liquid.user.domain.GroupMember;
import liquid.user.model.PasswordChange;
import liquid.user.model.User;

import java.util.Collection;

/**
 * Created by Tao Ma on 3/19/15.
 */
public interface UserService {
    Group addGroup(Group group);

    Collection<Group> findGroups();

    GroupMember findByUsername(String username);

    void register(User user);

    boolean authenticate(String userDn, String credentials);

    User find(String uid);

    Collection<User> findAll();

    void lock(String uid);

    void unlock(String uid);

    void edit(User user);

    void resetPassword(String uid, PasswordChange passwordChange);
}
