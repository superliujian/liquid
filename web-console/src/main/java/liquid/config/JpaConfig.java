package liquid.config;

import liquid.persistence.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
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
                        SpRepository.class,
                        LocationRepository.class,
                        CargoRepository.class,
                        ChargeTypeRepository.class,
                        ContainerRepository.class,
                        OrderRepository.class,
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
                        ChargeRepository.class},
                type = FilterType.ASSIGNABLE_TYPE))
@EnableTransactionManagement
public class JpaConfig {
    @Bean
    public DataSource dataSource() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        dataSource.setUrl("jdbc:mysql://localhost:3306/liquid_v2?autoReconnect=true");
        dataSource.setUsername("liquid");
        dataSource.setPassword("liquid");
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
