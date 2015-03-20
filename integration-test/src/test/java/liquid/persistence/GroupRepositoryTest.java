package liquid.persistence;

import liquid.user.db.config.UserJpaConfig;
import liquid.user.domain.Group;
import liquid.user.domain.GroupMember;
import liquid.user.db.repository.GroupMemberRepository;
import liquid.user.db.repository.GroupRepository;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Tao Ma on 3/18/15.
 */
public class GroupRepositoryTest {
    @Test
    public void addGroup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(UserJpaConfig.class);
        context.refresh();

        GroupRepository groupRepository = (GroupRepository) context.getBean(GroupRepository.class);
        Group group = new Group("MARKETING");
        groupRepository.save(group);
    }

    @Test
    public void addMemberToGroup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(UserJpaConfig.class);
        context.refresh();

        GroupRepository groupRepository = (GroupRepository) context.getBean(GroupRepository.class);
        GroupMemberRepository groupMemberRepository = (GroupMemberRepository) context.getBean(GroupMemberRepository.class);
        GroupMember member = new GroupMember("redbrick9", new Group(1L));
        groupMemberRepository.save(member);
    }

    @Test
    public void findGroup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(UserJpaConfig.class);
        context.refresh();

        GroupRepository groupRepository = (GroupRepository) context.getBean(GroupRepository.class);
        Group group = groupRepository.findOne(1L);
        System.out.println(group.getMembers().size());
    }

    @Test
    public void findAllGroup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(UserJpaConfig.class);
        context.refresh();

        GroupRepository groupRepository = (GroupRepository) context.getBean(GroupRepository.class);
        Iterable<Group> groups = groupRepository.findAll();
        for (Group group : groups) {
            System.out.println(group.getId());
        }
    }
}
