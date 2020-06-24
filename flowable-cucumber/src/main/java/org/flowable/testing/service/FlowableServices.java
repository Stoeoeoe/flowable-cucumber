package org.flowable.testing.service;

import org.flowable.app.api.AppManagementService;
import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.engine.AppEngine;
import org.flowable.app.engine.AppEngineConfiguration;
import org.flowable.cmmn.api.*;
import org.flowable.cmmn.engine.CmmnEngine;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.cmmn.engine.CmmnEngines;
import org.flowable.dmn.api.DmnHistoryService;
import org.flowable.dmn.api.DmnManagementService;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.dmn.api.DmnRuleService;
import org.flowable.dmn.engine.DmnEngine;
import org.flowable.dmn.engine.DmnEngineConfiguration;
import org.flowable.dmn.engine.DmnEngines;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.test.mock.MockExpressionManager;
import org.flowable.form.api.FormManagementService;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.api.FormService;
import org.flowable.form.engine.FormEngine;
import org.flowable.form.engine.FormEngineConfiguration;
import org.flowable.form.engine.FormEngines;

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
        this.appEngineConfiguration = AppEngineConfiguration.createAppEngineConfigurationFromResource("flowable.cfg.xml");

        appEngineConfiguration.setExpressionManager(new MockExpressionManager());

        // The lead engine will build all other engines
        this.appEngine = appEngineConfiguration.buildAppEngine();
        this.appRepositoryService = appEngine.getAppRepositoryService();
        this.appManagementService = appEngine.getAppManagementService();

        this.processEngine = ProcessEngines.getDefaultProcessEngine();
        this.processEngineConfiguration = this.processEngine.getProcessEngineConfiguration();
        ((ProcessEngineConfigurationImpl) this.processEngineConfiguration).setExpressionManager(new MockExpressionManager());
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
        this.repositoryService = processEngine.getRepositoryService();
        this.historyService = processEngine.getHistoryService();
        this.managementService = processEngine.getManagementService();
        this.processMigrationService = processEngine.getProcessMigrationService();
        this.identityService = processEngine.getIdentityService();

        this.cmmnEngine = CmmnEngines.getDefaultCmmnEngine();
        this.cmmnEngineConfiguration = this.cmmnEngine.getCmmnEngineConfiguration();
        this.cmmnEngineConfiguration.setExpressionManager(new MockExpressionManager());
        this.cmmnRuntimeService = cmmnEngine.getCmmnRuntimeService();
        this.cmmnTaskService = cmmnEngine.getCmmnTaskService();
        this.cmmnRepositoryService = cmmnEngine.getCmmnRepositoryService();
        this.cmmnHistoryService = cmmnEngine.getCmmnHistoryService();
        this.cmmnManagementService = cmmnEngine.getCmmnManagementService();

        this.dmnEngine = DmnEngines.getDefaultDmnEngine();
        this.dmnEngineConfiguration = this.dmnEngine.getDmnEngineConfiguration();
        this.dmnEngineConfiguration.setExpressionManager(new MockExpressionManager());
        this.dmnRepositoryService = dmnEngine.getDmnRepositoryService();
        this.dmnRuleService = dmnEngine.getDmnRuleService();
        this.dmnHistoryService = dmnEngine.getDmnHistoryService();
        this.dmnManagementService = dmnEngine.getDmnManagementService();

        this.formEngine = FormEngines.getDefaultFormEngine();
        this.formEngineConfiguration = this.formEngine.getFormEngineConfiguration();
        this.formEngineConfiguration.setExpressionManager(new MockExpressionManager());
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
