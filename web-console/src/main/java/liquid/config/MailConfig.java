package liquid.config;

import liquid.service.MailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * Created by redbrick9 on 2/18/14.
 */
@Configuration
@PropertySource("classpath:liquid.properties")
public class MailConfig {
    @Autowired
    private Environment env;

    @Bean
    public MailNotificationService mailNotificationService() {
        MailNotificationService mailNotificationService = new MailNotificationService();

        // Get system properties
        Properties properties = new Properties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", env.getProperty("mail.smtp.host"));
        properties.setProperty("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        properties.setProperty("mail.user", env.getProperty("mail.user"));
        properties.setProperty("mail.password", env.getProperty("mail.password"));
        properties.setProperty("mail.admin", env.getProperty("mail.admin"));

        mailNotificationService.setProperties(properties);

        return mailNotificationService;
    }
}
