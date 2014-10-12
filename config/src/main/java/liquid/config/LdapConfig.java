package liquid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/20/13
 * Time: 10:12 AM
 */
@Configuration
public class LdapConfig {

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        try {
            contextSource.setUrl("ldap://localhost:389");
            contextSource.setBase("dc=suncapital-logistics,dc=com");
            contextSource.setUserDn("cn=admin,dc=suncapital-logistics,dc=com");
            contextSource.setPassword("liquid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }
}
