package liquid.persistence;

import liquid.user.config.JpaConfig;
import liquid.user.persistence.domain.GroupEntity;
import liquid.user.persistence.domain.GroupMemberEntity;
import liquid.user.persistence.repository.GroupMemberRepository;
import liquid.user.persistence.repository.GroupRepository;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Tao Ma on 3/18/15.
 */
public class GroupRepositoryTest {
    @Test
    public void addGroup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        GroupRepository groupRepository = (GroupRepository) context.getBean(GroupRepository.class);
        GroupEntity group = new GroupEntity("MARKETING");
        groupRepository.save(group);
    }

    @Test
    public void addMemberToGroup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        GroupRepository groupRepository = (GroupRepository) context.getBean(GroupRepository.class);
        GroupMemberRepository groupMemberRepository = (GroupMemberRepository) context.getBean(GroupMemberRepository.class);
        GroupMemberEntity member = new GroupMemberEntity("redbrick9", new GroupEntity(1L));
        groupMemberRepository.save(member);
    }

    @Test
    public void findGroup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        GroupRepository groupRepository = (GroupRepository) context.getBean(GroupRepository.class);
        GroupEntity group = groupRepository.findOne(1L);
        System.out.println(group.getMembers().size());
    }

    @Test
    public void findAllGroup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        GroupRepository groupRepository = (GroupRepository) context.getBean(GroupRepository.class);
        Iterable<GroupEntity> groups = groupRepository.findAll();
        for (GroupEntity group : groups) {
            System.out.println(group.getId());
        }
    }
}
