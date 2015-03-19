package liquid.service;


import liquid.user.db.config.JpaConfig;
import liquid.user.db.persistence.domain.GroupEntity;
import liquid.user.service.UserService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Tao Ma on 3/18/15.
 */
public class UserServiceTest {

    @Test
    public void testFind() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.register(UserService.class);
        context.refresh();

        UserService userService = (UserService) context.getBean(UserService.class);
        GroupEntity group = userService.find(2L);
    }
}
