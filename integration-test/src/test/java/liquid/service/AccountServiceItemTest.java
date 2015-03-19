package liquid.service;

import liquid.config.LdapConfig;
import liquid.user.service.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by redbrick9 on 5/1/14.
 */
public class AccountServiceItemTest {
    @Test
    public void fetchPasswordPolicy() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(LdapConfig.class);
        context.register(MailNotificationService.class);
        context.register(UserServiceImpl.class);
        context.refresh();

        UserServiceImpl accountService = context.getBean(UserServiceImpl.class);
        Assert.assertEquals(3, accountService.fetchPasswordPolicy().getPwdMaxFailure());
    }
}
