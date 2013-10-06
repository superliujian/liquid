package liquid.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/21/13
 * Time: 11:47 AM
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LdapContextSource contextSource;

    @Override
    protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication().contextSource(contextSource)
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .groupSearchFilter("(uniqueMember={0})");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**"); // #3
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeUrls()
                .antMatchers("/register", "/about").permitAll() // #4
                .antMatchers("/admin/**").hasRole("ADMIN") // #6
                .antMatchers("/favicon.ico").hasRole("ANONYMOUS")
                .anyRequest().authenticated() // 7
                .and()
                .formLogin()  // #8
                .loginPage("/signin") // #9
                .permitAll() // #5
                .and()
                .logout()
                .logoutSuccessUrl("/signin");
    }

}
