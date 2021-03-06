package liquid.config;

import liquid.converter.GenericFormatter;
import liquid.interceptor.LoggingInterceptor;
import liquid.operation.converter.LocationTypeFormatter;
import liquid.operation.converter.ToServiceSubtypeConverter;
import liquid.operation.domain.ServiceProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.Locale;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/19/13
 * Time: 9:22 PM
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "liquid.controller",
        "liquid.api.controller",
        "liquid.user.controller",
        "liquid.operation.controller",
        "liquid.operation.restfulapi",
        "liquid.process.controller",
        "liquid.order.restfulapi",
        "liquid.order.controller",
        "liquid.transport.web.controller",
        "liquid.accounting.web.controller",
        "liquid.purchase.api.controller",
        "liquid.purchase.web.controller"})
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private GenericFormatter<ServiceProviderType> serviceProviderTypeFormatter;

    @Autowired
    private ToServiceSubtypeConverter toServiceSubtypeConverter;

    @Autowired
    private LocationTypeFormatter locationTypeFormatter;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(serviceProviderTypeFormatter);
        registry.addFormatter(locationTypeFormatter);
        registry.addConverter(toServiceSubtypeConverter);
    }

    @Bean
    public ServletContextTemplateResolver templateResolver() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        //NB, selecting HTML5 as the template mode.
        resolver.setTemplateMode("HTML5");
        resolver.setCacheable(false);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.addDialect(new SpringSecurityDialect());
        return engine;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setViewNames(new String[]{"*"});
        viewResolver.setCache(false);
        viewResolver.setContentType("text/html; charset=UTF-8");
        return viewResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages", "version");
        return messageSource;
    }

    @Bean
    public Locale locale() {
        return Locale.CHINA;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(locale());
        return localeResolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        MultipartResolver multipartResolver = new StandardServletMultipartResolver();
        return multipartResolver;
    }
}
