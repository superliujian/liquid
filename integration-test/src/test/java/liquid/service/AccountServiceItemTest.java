package liquid.service;

import liquid.config.LdapConfig;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.MessageSource;
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
        context.register(AccountService.class);
        context.refresh();

        AccountService accountService = context.getBean(AccountService.class);
        Assert.assertEquals(3, accountService.fetchPasswordPolicy().getPwdMaxFailure());
    }
}
