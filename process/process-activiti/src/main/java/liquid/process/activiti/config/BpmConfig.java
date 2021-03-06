package liquid.process.activiti.config;


import liquid.config.PropertyPlaceholderConfig;
import liquid.process.handler.ProcessEndHandler;
import org.activiti.engine.cfg.ProcessEngineConfigurator;
import org.activiti.ldap.LDAPConfigurator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/22/13
 * Time: 9:37 PM
 */
@Configuration
@Import(PropertyPlaceholderConfig.class)
public class BpmConfig {

    @Value("${bpm.jdbc.url}")
    private String jdbcUrl;
    @Value("${bpm.jdbc.username}")
    private String jdbcUsername;
    @Value("${bpm.jdbc.password}")
    private String jdbcPassword;

    @Bean
    public SimpleDriverDataSource bpmDataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager bpmTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(bpmDataSource());
        return transactionManager;
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDatabaseType("mysql");
        processEngineConfiguration.setDataSource(bpmDataSource());
        processEngineConfiguration.setTransactionManager(bpmTransactionManager());
        processEngineConfiguration.setDatabaseSchemaUpdate("true");

        Resource[] resources = new Resource[1];
        ClassPathResource classPathResource = new ClassPathResource("processes/liquid.poc.bpmn20.xml");
        resources[0] = classPathResource;
        processEngineConfiguration.setDeploymentResources(resources);
        processEngineConfiguration.setHistory("audit");
        processEngineConfiguration.setJobExecutorActivate(false);

//        List<ProcessEngineConfigurator> configurators = new ArrayList<>();
//
//        LDAPConfigurator configurator = new LDAPConfigurator();
//
//        // Server connection params
//        configurator.setServer("ldap://localhost");
//        configurator.setPort(389);
//        configurator.setUser("cn=admin,dc=suncapital-logistics,dc=com");
//        configurator.setPassword("liquid");
//        // Query params
//        configurator.setBaseDn("dc=suncapital-logistics,dc=com");
//        configurator.setQueryUserByUserId("(&(objectClass=inetOrgPerson)(uid={0}))");
//        configurator.setQueryUserByFullNameLike("(&(objectClass=inetOrgPerson)(|({0}=*{1}*)({2}=*{3}*)))");
//        configurator.setQueryGroupsForUser("(&(objectClass=groupOfNames)(member={0}))");
//        // Attribute config
//        configurator.setUserIdAttribute("uid");
//        configurator.setUserFirstNameAttribute("cn");
//        configurator.setUserLastNameAttribute("sn");
//        configurator.setGroupIdAttribute("cn");
//        configurator.setGroupNameAttribute("cn");
//        configurators.add(configurator);
//
//        processEngineConfiguration.setConfigurators(configurators);

        Map<Object, Object> beans = new HashMap<>();
        beans.put("processEndHandler", processEndHandler());
        processEngineConfiguration.setBeans(beans);

        return processEngineConfiguration;
    }

    @Bean
    public ProcessEngineFactoryBean processEngine() throws Exception {
        ProcessEngineFactoryBean processEngineFactory = new ProcessEngineFactoryBean();
        processEngineFactory.setProcessEngineConfiguration(processEngineConfiguration());
        return processEngineFactory;
    }

    @Bean
    public ProcessEndHandler processEndHandler() {
        return new ProcessEndHandler();
    }
}
