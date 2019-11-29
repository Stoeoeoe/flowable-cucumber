package com.flowable.testing.glue;

import java.util.List;
import java.util.logging.Logger;

import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.api.repository.AppDeployment;
import org.flowable.cmmn.api.CmmnRepositoryService;
import org.flowable.cmmn.api.repository.CmmnDeployment;
import org.flowable.common.engine.impl.el.ExpressionManager;
import org.flowable.dmn.api.DmnDeployment;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.history.HistoryManager;
import org.flowable.engine.impl.test.JobTestHelper;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.test.mock.MockExpressionManager;
import org.flowable.engine.test.mock.Mocks;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.spring.SpringProcessEngineConfiguration;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java8.En;

public class Hooks implements En {

    Logger logger = Logger.getLogger(Hooks.class.getName());

    // TODO: Only needed to set MockExpressionManager, but probably Spring Users should simply use @MockBean. Then, this could be used with ProcessEngineServiceImpl...
    private final SpringProcessEngineConfiguration processEngineConfiguration;

    private final RepositoryService repositoryService;
    private final CmmnRepositoryService cmmnRepositoryService;
    private final AppRepositoryService appRepositoryService;
    private final DmnRepositoryService dmnRepositoryService;
    private final DmnRepositoryService formRepositoryService;
    private final IdentityService identityService;
    private final ManagementService managementService;
    private final HistoryService historyService;

    private ExpressionManager originalExpressionManager;
    private final int asyncTimeout;
    private final int asyncInterval;

    public Hooks(SpringProcessEngineConfiguration processEngineConfiguration, RepositoryService repositoryService,
        CmmnRepositoryService cmmnRepositoryService,
        AppRepositoryService appRepositoryService, DmnRepositoryService dmnRepositoryService, DmnRepositoryService formRepositoryService,
        IdentityService identityService, ManagementService managementService,
        //, TODO: Why doesn't this work? :(
        //        @Value("${flowable.testing.async-timeout}") int asyncTimeout,
        //        @Value("${flowable.testing.async-interval}") int asyncInterval
        HistoryService historyService) {
        this.processEngineConfiguration = processEngineConfiguration;
        this.repositoryService = repositoryService;
        this.cmmnRepositoryService = cmmnRepositoryService;
        this.appRepositoryService = appRepositoryService;
        this.dmnRepositoryService = dmnRepositoryService;
        this.formRepositoryService = formRepositoryService;
        this.identityService = identityService;
        this.managementService = managementService;

        this.originalExpressionManager = processEngineConfiguration.getExpressionManager();
        this.historyService = historyService;

        this.asyncInterval = 100;
        this.asyncTimeout = 1000;
    }

    @Before(order = 0)
    public void resetMocks() {
        Mocks.reset();
    }

    @Before(order = 10)
    public void deleteAllIdentityInformation() {
        List<User> users = identityService.createUserQuery().list();
        for (User user : users) {
            identityService.deleteUser(user.getId());
        }

        List<Group> groups = identityService.createGroupQuery().list();
        for (Group group : groups) {
            identityService.deleteGroup(group.getId());
        }
    }


    @Before(order = 20)
    public void deleteHistory() {
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
        }
        // TODO: Use new history cleaner in 6.5
    }


    @Before(order = 30)
    public void deleteAllDeployments() {
        appRepositoryService.createDeploymentQuery().list().stream().map(AppDeployment::getId).forEach(id -> appRepositoryService.deleteDeployment(id, true));
        repositoryService.createDeploymentQuery().list().stream().map(Deployment::getId).forEach(id -> repositoryService.deleteDeployment(id, true));
        cmmnRepositoryService.createDeploymentQuery().list().stream().map(CmmnDeployment::getId).forEach(id -> cmmnRepositoryService.deleteDeployment(id, true));
        dmnRepositoryService.createDeploymentQuery().list().stream().map(DmnDeployment::getId).forEach(id -> dmnRepositoryService.deleteDeployment(id));
        formRepositoryService.createDeploymentQuery().list().stream().map(DmnDeployment::getId).forEach(id -> formRepositoryService.deleteDeployment(id));
    }

    // TODO: This doesn't work :)
    @Before(value = "@Mock", order = 40)
    public void useMockExpressionManager() {
        MockExpressionManager mockExpressionManager = new MockExpressionManager();
        processEngineConfiguration.setExpressionManager(mockExpressionManager);
    }


    @AfterStep(value = "@Async", order = 0)
    public void executeAsyncJobs() {
        JobTestHelper.waitForJobExecutorToProcessAllJobs(processEngineConfiguration, managementService, asyncTimeout, asyncInterval, true);
    }



    @After(value = "@Mock", order = 10)
    public void resetExpressionManager() {
        processEngineConfiguration.setExpressionManager(originalExpressionManager);
        Mocks.reset();
    }



}
