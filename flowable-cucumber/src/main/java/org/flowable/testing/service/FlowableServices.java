package org.flowable.testing.service;

import org.flowable.app.api.AppManagementService;
import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.engine.AppEngine;
import org.flowable.app.engine.AppEngineConfiguration;
import org.flowable.cmmn.api.*;
import org.flowable.cmmn.engine.CmmnEngine;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.dmn.api.DmnHistoryService;
import org.flowable.dmn.api.DmnManagementService;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.dmn.api.DmnRuleService;
import org.flowable.dmn.engine.DmnEngine;
import org.flowable.dmn.engine.DmnEngineConfiguration;
import org.flowable.engine.*;
import org.flowable.form.api.FormManagementService;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.api.FormService;
import org.flowable.form.engine.FormEngine;
import org.flowable.form.engine.FormEngineConfiguration;

public class FlowableServices {


    private final ProcessEngineConfiguration processEngineConfiguration;
    private final ProcessEngine processEngine;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final ManagementService managementService;
    private final ProcessMigrationService processMigrationService;

    private final CmmnEngineConfiguration cmmnEngineConfiguration;
    private final CmmnEngine cmmnEngine;
    private final CmmnRuntimeService cmmnRuntimeService;
    private final CmmnTaskService cmmnTaskService;
    private final CmmnRepositoryService cmmnRepositoryService;
    private final CmmnHistoryService cmmnHistoryService;
    private final CmmnManagementService cmmnManagementService;

    private final DmnEngineConfiguration dmnEngineConfiguration;
    private final DmnEngine dmnEngine;
    private final DmnRepositoryService dmnRepositoryService;
    private final DmnRuleService dmnRuleService;
    private final DmnHistoryService dmnHistoryService;
    private final DmnManagementService dmnManagementService;

    private final AppEngineConfiguration appEngineConfiguration;
    private final AppEngine appEngine;
    private final AppRepositoryService appRepositoryService;
    private final AppManagementService appManagementService;

    private final FormEngineConfiguration formEngineConfiguration;
    private final FormEngine formEngine;
    private final FormRepositoryService formRepositoryService;
    private final FormService formService;
    private final FormManagementService formManagementService;

    private final IdentityService identityService;


    public FlowableServices() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("cucumber-engine-config/flowable-test.cfg.xml");
        CmmnEngineConfiguration cmmnEngineConfiguration  = CmmnEngineConfiguration.createCmmnEngineConfigurationFromResource("cucumber-engine-config/flowable-test.cmmn.cfg.xml");
        DmnEngineConfiguration dmnEngineConfiguration  = DmnEngineConfiguration.createDmnEngineConfigurationFromResource("cucumber-engine-config/flowable-test.dmn.cfg.xml");
        AppEngineConfiguration appEngineConfiguration = AppEngineConfiguration.createAppEngineConfigurationFromResource("cucumber-engine-config/flowable-test.app.cfg.xml");
        FormEngineConfiguration formEngineConfiguration = FormEngineConfiguration.createFormEngineConfigurationFromResource("cucumber-engine-config/flowable-test.form.cfg.xml");

        this.processEngine = processEngineConfiguration.buildProcessEngine();
        this.processEngineConfiguration = processEngineConfiguration;
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
        this.repositoryService = processEngine.getRepositoryService();
        this.historyService = processEngine.getHistoryService();
        this.managementService = processEngine.getManagementService();
        this.processMigrationService = processEngine.getProcessMigrationService();
        this.identityService = processEngine.getIdentityService();

        this.cmmnEngine = cmmnEngineConfiguration.buildCmmnEngine();
        this.cmmnEngineConfiguration = cmmnEngineConfiguration;
        this.cmmnRuntimeService = cmmnEngine.getCmmnRuntimeService();
        this.cmmnTaskService = cmmnEngine.getCmmnTaskService();
        this.cmmnRepositoryService = cmmnEngine.getCmmnRepositoryService();
        this.cmmnHistoryService = cmmnEngine.getCmmnHistoryService();
        this.cmmnManagementService = cmmnEngine.getCmmnManagementService();

      this.dmnEngine = dmnEngineConfiguration.buildDmnEngine();
        this.dmnEngineConfiguration = dmnEngineConfiguration;
        this.dmnRepositoryService = dmnEngine.getDmnRepositoryService();
        this.dmnRuleService = dmnEngine.getDmnRuleService();
        this.dmnHistoryService = dmnEngine.getDmnHistoryService();
        this.dmnManagementService = dmnEngine.getDmnManagementService();

        this.appEngine = appEngineConfiguration.buildAppEngine();
        this.appEngineConfiguration = appEngineConfiguration;
        this.appRepositoryService = appEngine.getAppRepositoryService();
        this.appManagementService = appEngine.getAppManagementService();

        this.formEngine = formEngineConfiguration.buildFormEngine();
        this.formEngineConfiguration = formEngineConfiguration;
        this.formRepositoryService = formEngine.getFormRepositoryService();
        this.formService = formEngine.getFormService();
        this.formManagementService = formEngine.getFormManagementService();
    }

    public ProcessEngineConfiguration getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public RepositoryService getRepositoryService() {
        return repositoryService;
    }

    public HistoryService getHistoryService() {
        return historyService;
    }

    public ManagementService getManagementService() {
        return managementService;
    }

    public ProcessMigrationService getProcessMigrationService() {
        return processMigrationService;
    }

    public CmmnEngineConfiguration getCmmnEngineConfiguration() {
        return cmmnEngineConfiguration;
    }

    public CmmnEngine getCmmnEngine() {
        return cmmnEngine;
    }

    public CmmnRuntimeService getCmmnRuntimeService() {
        return cmmnRuntimeService;
    }

    public CmmnTaskService getCmmnTaskService() {
        return cmmnTaskService;
    }

    public CmmnRepositoryService getCmmnRepositoryService() {
        return cmmnRepositoryService;
    }

    public CmmnHistoryService getCmmnHistoryService() {
        return cmmnHistoryService;
    }

    public CmmnManagementService getCmmnManagementService() {
        return cmmnManagementService;
    }

    public DmnEngineConfiguration getDmnEngineConfiguration() {
        return dmnEngineConfiguration;
    }

    public DmnEngine getDmnEngine() {
        return dmnEngine;
    }

    public DmnRepositoryService getDmnRepositoryService() {
        return dmnRepositoryService;
    }

    public DmnRuleService getDmnRuleService() {
        return dmnRuleService;
    }

    public DmnHistoryService getDmnHistoryService() {
        return dmnHistoryService;
    }

    public DmnManagementService getDmnManagementService() {
        return dmnManagementService;
    }

    public AppEngineConfiguration getAppEngineConfiguration() {
        return appEngineConfiguration;
    }

    public AppEngine getAppEngine() {
        return appEngine;
    }

    public AppRepositoryService getAppRepositoryService() {
        return appRepositoryService;
    }

    public AppManagementService getAppManagementService() {
        return appManagementService;
    }

    public FormEngineConfiguration getFormEngineConfiguration() {
        return formEngineConfiguration;
    }

    public FormEngine getFormEngine() {
        return formEngine;
    }

    public FormRepositoryService getFormRepositoryService() {
        return formRepositoryService;
    }

    public FormService getFormService() {
        return formService;
    }

    public FormManagementService getFormManagementService() {
        return formManagementService;
    }

    public IdentityService getIdentityService() {
        return identityService;
    }
}
