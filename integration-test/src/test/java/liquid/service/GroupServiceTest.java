package liquid.service;


import liquid.user.config.JpaConfig;
import liquid.user.persistence.domain.GroupEntity;
import liquid.user.service.GroupService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Tao Ma on 3/18/15.
 */
public class GroupServiceTest {

    @Test
    public void testFind() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.register(GroupService.class);
        context.refresh();

        GroupService groupService = (GroupService) context.getBean(GroupService.class);
        GroupEntity group = groupService.find(2L);
    }
}
