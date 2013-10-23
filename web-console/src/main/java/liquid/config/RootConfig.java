package liquid.config;

import liquid.context.BusinessContext;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.SimpleThreadScope;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/22/13
 * Time: 11:24 PM
 */
@Configuration
@ComponentScan({"liquid.aop", "liquid.service", "liquid.task.handler"})
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
    @Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public BusinessContext businessContext() {
        BusinessContext businessContext = new BusinessContext();
        return businessContext;
    }
}
