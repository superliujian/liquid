package liquid.config;

import liquid.persistence.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/24/13
 * Time: 9:06 PM
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "liquid.persistence.repository",
        includeFilters = @ComponentScan.Filter(
                value = {
                        CustomerRepository.class,
                        ServiceProviderRepository.class,
                        ServiceProviderTypeRepository.class,
                        ServiceTypeRepository.class,
                        ServiceSubtypeRepository.class,
                        ServiceItemRepository.class,
                        ContainerSubtypeRepository.class,
                        SequenceRepository.class,
                        LocationRepository.class,
                        GoodsRepository.class,
                        ChargeTypeRepository.class,
                        ContainerRepository.class,
                        OrderRepository.class,
                        OrderHistoryRepository.class,
                        IncomeRepository.class,
                        ReceivingOrderRepository.class,
                        ReceivingContainerRepository.class,
                        PlanningRepository.class,
                        RouteRepository.class,
                        LegRepository.class,
                        ShippingContainerRepository.class,
                        RailContainerRepository.class,
                        BargeContainerRepository.class,
                        VesselContainerRepository.class,
                        DeliveryContainerRepository.class,
                        ChargeRepository.class,
                        ServiceItemRepository.class,
                        ExchangeRateRepository.class,
                        AuthorRepository.class},
                type = FilterType.ASSIGNABLE_TYPE))
@EnableTransactionManagement
@Import(PropertyPlaceholderConfig.class)
public class JpaConfig {
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Bean
    public DataSource dataSource() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("javax.persistence.validation.mode", "none");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("liquid.persistence.domain");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(jpaProperties);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }
}
