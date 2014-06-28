package liquid.container.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by redbrick9 on 6/27/14.
 */
@Configuration
@EnableJpaRepositories("liquid.container.persistence.repository")
@EnableTransactionManagement
public class JpaConfig {}
