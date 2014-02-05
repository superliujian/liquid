package liquid.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
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

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                               AccessDeniedException e) throws IOException, ServletException {
                response.sendRedirect("error/403");
                request.getSession().setAttribute("message",
                        "You do not have permission to access this page!");
            }
        };
        return accessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/account/register", "/about").permitAll() // #4
                .antMatchers("/admin/**").hasRole("ADMIN") // #6
                .antMatchers("/container/**").hasRole("CONTAINER")
                .antMatchers("/task/*/planning/**").hasRole("MARKETING")
                .antMatchers("/favicon.ico").hasRole("ANONYMOUS")
                .anyRequest().authenticated() // 7
                .and()
                .formLogin()  // #8
                .loginPage("/login") // #9
                .permitAll() // #5
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }
}
