package liquid.user.db.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tao Ma on 3/16/15.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
    @Autowired
    @Qualifier("userDataSource")
    private DataSource dataSource;

    @Autowired
    private ApplicationContext context;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                eraseCredentials(false).
                jdbcAuthentication().dataSource(dataSource).
                passwordEncoder(passwordEncoder()).
                getUserDetailsService().setEnableGroups(true);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**");
    }

    @Bean
    public ExceptionMappingAuthenticationFailureHandler authenticationFailureHandler() {
        final ExceptionMappingAuthenticationFailureHandler authenticationFailureHandler =
                new ExceptionMappingAuthenticationFailureHandler();
        final Map<String, String> mappings = new HashMap<>();

        mappings.put(BadCredentialsException.class.getCanonicalName(), "/login?error=login.failure.badcredentials");

        authenticationFailureHandler.setExceptionMappings(mappings);
        return authenticationFailureHandler;
    }

//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return new AuthenticationFailureHandler() {
//            @Override
//            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
////                FlashMapManager flashMapManager = new SessionFlashMapManager();
//                FlashMapManager flashMapManager =
//                        context.getBean("flashMapManager", FlashMapManager.class);
//                FlashMap flashMap = new FlashMap();
//                flashMap.put("alert", "login.failure.badcredentials");
//                flashMapManager.saveOutputFlashMap(flashMap, request, response);
//                response.sendRedirect("/login");
//            }
//        };
//    }

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
                .antMatchers("/account/register", "/login", "/about", "/error/**", "/test/**").permitAll() // #4
                .antMatchers("/admin/**").hasRole("ADMIN") // #6
                .antMatchers("/container/**").hasRole("CONTAINER")
                .antMatchers("/task/*/planning/**").hasRole("MARKETING")
                .antMatchers("/favicon.ico").hasRole("ANONYMOUS")
                .anyRequest().authenticated() // 7
                .and()
                .formLogin()  // #8
                .loginPage("/login") // #9
                .failureHandler(authenticationFailureHandler())
                .permitAll() // #5
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
