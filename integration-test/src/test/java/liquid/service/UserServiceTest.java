package liquid.service;


import liquid.user.db.config.UserJpaConfig;
import liquid.user.domain.Group;
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
        context.register(UserJpaConfig.class);
        context.register(UserService.class);
        context.refresh();

        UserService userService = (UserService) context.getBean(UserService.class);
        Group group = userService.find(2);
    }
}
