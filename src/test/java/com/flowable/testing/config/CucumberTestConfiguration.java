package com.flowable.testing.config;

import com.flowable.testing.service.CaseTestService;
import com.flowable.testing.service.ProcessTestService;
import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.engine.AppEngine;
import org.flowable.app.engine.AppEngineConfiguration;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnRepositoryService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.cmmn.engine.CmmnEngine;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.dmn.api.DmnRuleService;
import org.flowable.dmn.engine.DmnEngine;
import org.flowable.dmn.engine.DmnEngineConfiguration;
import org.flowable.engine.*;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.api.FormService;
import org.flowable.form.engine.FormEngine;
import org.flowable.form.engine.FormEngineConfiguration;
import org.flowable.form.engine.configurator.FormEngineConfigurator;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.engine.IdmEngine;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootConfiguration
public class CucumberTestConfiguration {

    private final String IN_MEMORY_DB_ADDRESS = "jdbc:h2:mem:cucumber-test-db;DB_CLOSE_DELAY=1000";

    // Engines
    @Bean
    public FormEngine formEngine () {
        FormEngineConfiguration formEngineConfiguration = (FormEngineConfiguration) FormEngineConfiguration.createFormEngineConfigurationFromResource("cucumber.flowable.form.cfg.xml")
                .setJdbcUrl(IN_MEMORY_DB_ADDRESS);
        return formEngineConfiguration.buildFormEngine();
    }

    @Bean
    public ProcessEngine processEngine(FormEngine formEngine) {
        FormEngineConfigurator formEngineConfigurator = new FormEngineConfigurator();
        formEngineConfigurator.setFormEngineConfiguration(formEngine.getFormEngineConfiguration());

        ProcessEngineConfiguration processEngineConfiguration = (ProcessEngineConfiguration)ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("cucumber.flowable.cfg.xml")
                .setJdbcUrl(IN_MEMORY_DB_ADDRESS)
                .setConfigurators(Collections.singletonList(formEngineConfigurator));
        return processEngineConfiguration.buildProcessEngine();
    }

    @Bean
    public CmmnEngine cmmnEngine(FormEngine formEngine) {
        FormEngineConfigurator formEngineConfigurator = new FormEngineConfigurator();
        formEngineConfigurator.setFormEngineConfiguration(formEngine.getFormEngineConfiguration());

        CmmnEngineConfiguration cmmnEngineConfiguration = (CmmnEngineConfiguration) CmmnEngineConfiguration.createCmmnEngineConfigurationFromResource("cucumber.flowable.cmmn.cfg.xml")
                .setJdbcUrl(IN_MEMORY_DB_ADDRESS)
                .setConfigurators(Collections.singletonList(formEngineConfigurator));


        return cmmnEngineConfiguration.buildCmmnEngine();
    }

    @Bean
    public IdmEngine idmEngine() {
        IdmEngineConfiguration idmEngineConfiguration = (IdmEngineConfiguration) IdmEngineConfiguration.createIdmEngineConfigurationFromResource("cucumber.flowable.idm.cfg.xml")
                .setJdbcUrl(IN_MEMORY_DB_ADDRESS);
        return idmEngineConfiguration.buildIdmEngine();
    }

    @Bean
    public DmnEngine dmnEngine() {
        DmnEngineConfiguration dmnEngineConfiguration = DmnEngineConfiguration.createDmnEngineConfigurationFromResource("cucumber.flowable.dmn.cfg.xml")
                .setJdbcUrl(IN_MEMORY_DB_ADDRESS);
        return dmnEngineConfiguration.buildDmnEngine();
    }

    @Bean
    public AppEngine appEngine() {
        AppEngineConfiguration appEngineConfiguration = (AppEngineConfiguration) AppEngineConfiguration.createAppEngineConfigurationFromResource("cucumber.flowable.app.cfg.xml")
                .setJdbcUrl(IN_MEMORY_DB_ADDRESS);
        return appEngineConfiguration.buildAppEngine();
    }


    // Process services
    @Bean
    public RuntimeService runtimeService(final ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public RepositoryService repositoryService(final ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public TaskService taskService(final ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }


    @Bean
    public HistoryService historyService(final ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    // CMMN services
    @Bean
    public CmmnRuntimeService cmmnRuntimeService(final CmmnEngine cmmnEngine) {
        return cmmnEngine.getCmmnRuntimeService();
    }

    @Bean
    public CmmnRepositoryService cmmnRepositoryService(final CmmnEngine cmmnEngine) {
        return cmmnEngine.getCmmnRepositoryService();
    }

    @Bean
    public CmmnHistoryService cmmnHistoryService(final CmmnEngine cmmnEngine) {
        return cmmnEngine.getCmmnHistoryService();
    }

    @Bean
    public CmmnTaskService cmmnTaskService(final CmmnEngine cmmnEngine) {
        return cmmnEngine.getCmmnTaskService();
    }



    // Form services
    @Bean
    public FormService formService(final FormEngine formEngine) {
        return formEngine.getFormService();
    }

    @Bean
    public FormRepositoryService formRepositoryService(final FormEngine formEngine) {
        return formEngine.getFormRepositoryService();
    }

    // App services
    @Bean
    public AppRepositoryService appRepositoryService(final AppEngine appEngine) {
        return appEngine.getAppRepositoryService();
    }

    // IDM services
    @Bean
    public IdmIdentityService idmIdentityService(final IdmEngine idmEngine) {
        return idmEngine.getIdmIdentityService();
    }

    @Bean
    public IdentityService identityService(final ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    // DMN services
    @Bean
    public DmnRepositoryService dmnRepositoryService(final DmnEngine dmnEngine) {
        return dmnEngine.getDmnRepositoryService();
    }

    @Bean
    public DmnRuleService dmnRuleService(final DmnEngine dmnEngine) {
        return dmnEngine.getDmnRuleService();
    }


    // Test services
    @Bean
    public ProcessTestService processTestService() {
        return new ProcessTestService();
    }

    @Bean
    public CaseTestService caseTestService() { return new CaseTestService(); }

}
