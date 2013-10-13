package liquid.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.ldap.core.LdapTemplate;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/21/13
 * Time: 9:43 PM
 */
@RunWith(JUnit4.class)
public class LdapConfigTest {
    @Test
    public void authenticate() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LdapConfig.class);
        ctx.refresh();

        LdapTemplate ldapTemplate = ctx.getBean(LdapTemplate.class);

        boolean authenticated = ldapTemplate.authenticate("", "(uid=lixg)", "111111");
    }
}
