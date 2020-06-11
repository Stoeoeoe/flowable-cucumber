package org.flowable.testing.config;

import static org.flowable.common.engine.impl.AbstractEngineConfiguration.DB_SCHEMA_UPDATE_FALSE;

import java.util.ArrayList;
import java.util.List;

import org.flowable.cmmn.spring.SpringCmmnEngineConfiguration;
import org.flowable.common.engine.impl.EngineConfigurator;
import org.flowable.common.engine.impl.util.TestClockImpl;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.form.engine.configurator.FormEngineConfigurator;
import org.flowable.form.spring.SpringFormEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.testing.service.CucumberCaseTestService;
import org.flowable.testing.service.CucumberProcessTestService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;


@SpringBootConfiguration
public class CucumberTestConfiguration {

    /*
            public FormEngineConfigurator formEngineConfigurator(SpringFormEngineConfiguration configuration) {
            SpringFormEngineConfigurator formEngineConfigurator = new SpringFormEngineConfigurator();
            formEngineConfigurator.setFormEngineConfiguration(configuration);
            invokeConfigurers(configuration);

            return formEngineConfigurator;
     */
    @Bean
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> testProcessEngineConfiguration(
        @Qualifier("formEngineConfigurator") FormEngineConfigurator formEngineConfigurator) {
        return configuration -> {
            configuration.setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_FALSE);
            configuration.setAsyncExecutorActivate(false);
            configuration.setClock(new TestClockImpl());
            //configuration.setExpressionManager(new MockExpressionManager());

            List<EngineConfigurator> engineConfigurators = new ArrayList<>();
            engineConfigurators.add(formEngineConfigurator);
            configuration.setConfigurators(engineConfigurators);

        };
    }

    @Bean
    public EngineConfigurationConfigurer<SpringCmmnEngineConfiguration> testCmmnEngineConfiguration() {
        return configuration -> {
            configuration.setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_FALSE);
            configuration.setAsyncExecutorActivate(false);
            configuration.setClock(new TestClockImpl());
       //     configuration.setExpressionManager(new MockExpressionManager());
        };
    }

    @Bean
    public EngineConfigurationConfigurer<SpringFormEngineConfiguration> testFormEngineConfiguration() {
        return configuration -> {
            configuration.setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_FALSE);
         //   configuration.setExpressionManager(new MockExpressionManager());
        };
    }


    @Bean
    public CucumberCaseTestService cucumberCaseTestService() {
        return new CucumberCaseTestService();
    }

    @Bean
    public CucumberProcessTestService cucumberProcessTestService(RepositoryService repositoryService, RuntimeService runtimeService) {
        return new CucumberProcessTestService(repositoryService, runtimeService);
    }


}
