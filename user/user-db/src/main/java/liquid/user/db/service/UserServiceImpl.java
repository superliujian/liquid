package liquid.user.db.service;

import liquid.user.db.repository.GroupMemberRepository;
import liquid.user.db.repository.GroupRepository;
import liquid.user.db.repository.UserProfileRepository;
import liquid.user.db.repository.UserRepository;
import liquid.user.domain.Group;
import liquid.user.domain.GroupMember;
import liquid.user.domain.UserProfile;
import liquid.user.model.PasswordChange;
import liquid.user.model.User;
import liquid.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Tao Ma on 3/19/15.
 */
@Service("db")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Transactional
    public Group find(Integer id) {
        Group group = groupRepository.findOne(id);
        if (null != group) {
            group.getMembers().size();
        }
        return group;
    }

    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group addGroup(Group group) {
        return null;
    }

    @Override
    public Collection<Group> findGroups() {
        return groupRepository.findAll();
    }

    @Transactional("userTransactionManager")
    @Override
    public void register(User user) {
        liquid.user.domain.User userEntity = new liquid.user.domain.User();
        UserProfile profile = new UserProfile();
        userEntity.setUsername(user.getUid());
        userEntity.setPassword(user.getPassword());
        userEntity.setEnabled(false);
        profile.setUser(userEntity);
        profile.setFirstName(user.getGivenName());
        profile.setLastName(user.getSurname());
        profile.setEmail(user.getEmail());
        profile.setCell(user.getCell());
        profile.setPhone(user.getPhone());
        userEntity.setProfile(profile);
        userRepository.save(userEntity);

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(new Group(Integer.valueOf(user.getGroup())));
        groupMember.setUsername(user.getUid());
        groupMemberRepository.save(groupMember);
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
        List<User> users = new ArrayList<User>();
        Iterable<liquid.user.domain.User> userEntities = findAll0();
        for (liquid.user.domain.User userEntity : userEntities) {
            User user = new User();
            user.setUid(userEntity.getUsername());
            user.setGivenName(userEntity.getProfile().getFirstName());
            user.setSurname(userEntity.getProfile().getLastName());
            user.setEmail(userEntity.getProfile().getEmail());
            user.setCell(userEntity.getProfile().getCell());
            user.setPhone(userEntity.getProfile().getPhone());

            users.add(user);
        }

        return users;
    }

    @Transactional("userTransactionManager")
    private Iterable<liquid.user.domain.User> findAll0() {
        Iterable<liquid.user.domain.User> users = userRepository.findAll();
        for (liquid.user.domain.User user : users) {
            user.getProfile();
        }
        return users;
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
