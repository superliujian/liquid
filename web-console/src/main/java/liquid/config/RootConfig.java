package liquid.config;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.support.SimpleThreadScope;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/22/13
 * Time: 11:24 PM
 */
@Configuration
@ComponentScan({"liquid.aop",
        "liquid.facade", "liquid.service",
        "liquid.excel",
        "liquid.container.facade", "liquid.container.service",
        "liquid.order.service",
        "liquid.task", "liquid.task.handler"})
@EnableAspectJAutoProxy
public class RootConfig {
    @Bean
    public SimpleThreadScope threadScope() {
        SimpleThreadScope threadScope = new SimpleThreadScope();
        return threadScope;
    }

    @Bean
    public CustomScopeConfigurer scopeConfigurer() {
        CustomScopeConfigurer scopeConfigurer = new CustomScopeConfigurer();
        Map<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("thread", threadScope());
        scopeConfigurer.setScopes(scopes);
        return scopeConfigurer;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("ServiceMessages");
        return messageSource;
    }

    @Bean
    public Locale locale() {
        return Locale.CHINA;
    }
}
