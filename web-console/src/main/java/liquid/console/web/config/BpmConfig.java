package liquid.console.web.config;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurator;
import org.activiti.ldap.LDAPConfigurator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/22/13
 * Time: 9:37 PM
 */
@Configuration
public class BpmConfig {

    @Autowired
    private SimpleDriverDataSource dataSource;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private SpringProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    private ProcessEngine processEngine;

    @Bean
    public SimpleDriverDataSource ldapTemplate() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/activiti?autoReconnect=true");
        dataSource.setUsername("activiti");
        dataSource.setPassword("activiti");
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDatabaseType("mysql");
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setTransactionManager(transactionManager);
        processEngineConfiguration.setDatabaseSchemaUpdate("true");

        Resource[] resources = new Resource[1];
        ClassPathResource classPathResource = new ClassPathResource("processes/liquid.poc.bpmn20.xml");
        resources[0] = classPathResource;
        processEngineConfiguration.setDeploymentResources(resources);
        processEngineConfiguration.setHistory("audit");
        processEngineConfiguration.setJobExecutorActivate(false);

        LDAPConfigurator configurator = new LDAPConfigurator();

        // Server connection params
        configurator.setServer("ldap://localhost");
        configurator.setPort(389);
        configurator.setUser("cn=admin,dc=suncapital-logistics,dc=com");
        configurator.setPassword("liquid");
        // Query params
        configurator.setBaseDn("dc=suncapital-logistics,dc=com");
        configurator.setQueryUserByUserId("(&(objectClass=inetOrgPerson)(uid={0}))");
        configurator.setQueryUserByFullNameLike("(&(objectClass=inetOrgPerson)(|({0}=*{1}*)({2}=*{3}*)))");
        configurator.setQueryGroupsForUser("(&(objectClass=groupOfNames)(member={0}))");
        // Attribute config
        configurator.setUserIdAttribute("uid");
        configurator.setUserFirstNameAttribute("cn");
        configurator.setUserLastNameAttribute("sn");
        configurator.setGroupIdAttribute("cn");
        configurator.setGroupNameAttribute("cn");

        List<ProcessEngineConfigurator> configurators = new ArrayList<>();
        configurators.add(configurator);
        processEngineConfiguration.setConfigurators(configurators);

        return processEngineConfiguration;
    }

    @Bean
    public ProcessEngineFactoryBean processEngine() {
        ProcessEngineFactoryBean processEngine = new ProcessEngineFactoryBean();
        processEngine.setProcessEngineConfiguration(processEngineConfiguration);
        return processEngine;
    }
}
