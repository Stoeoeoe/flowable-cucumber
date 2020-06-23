package org.flowable.testing.glue;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import org.flowable.app.api.repository.AppDeployment;
import org.flowable.cmmn.api.repository.CmmnDeployment;
import org.flowable.common.engine.impl.el.ExpressionManager;
import org.flowable.dmn.api.DmnDeployment;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.test.JobTestHelper;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.test.mock.Mocks;
import org.flowable.form.api.FormDeployment;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.testing.service.FlowableServices;

import java.util.List;

public class Hooks {

    private final FlowableServices flw;

    // TODO: Only needed to set MockExpressionManager, but probably Spring Users should simply use @MockBean. Then, this could be used with ProcessEngineServiceImpl...
    //private final ProcessEngineConfigurationImpl processEngineConfiguration;

    private ExpressionManager originalExpressionManager;
    private final int asyncTimeout;
    private final int asyncInterval;

    public Hooks(FlowableServices flw) {
        this.flw = flw;

        /*
        this.originalExpressionManager = processEngineConfiguration.getExpressionManager();
        this.historyService = flw.getHistoryService();
*/
        this.asyncInterval = 100;
        this.asyncTimeout = 1000;
    }

    /*
    TODO: Set up an engine through tags?
    @Before(value = "@InMemory", order = 0)
    public void setUpInMemoryEngines() {

    }
     */

    @Before(order = 10)
    public void resetMocks() {
        Mocks.reset();
    }

    @Before(order = 20)
    public void deleteAllIdentityInformation() {
        List<User> users = flw.getIdentityService().createUserQuery().list();
        for (User user : users) {
            flw.getIdentityService().deleteUser(user.getId());
        }

        List<Group> groups = flw.getIdentityService().createGroupQuery().list();
        for (Group group : groups) {
            flw.getIdentityService().deleteGroup(group.getId());
        }
    }


    @Before(order = 30)
    public void deleteHistory() {
        List<HistoricProcessInstance> historicProcessInstances = flw.getHistoryService().createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            flw.getHistoryService().deleteHistoricProcessInstance(historicProcessInstance.getId());
        }
        // TODO: Use new history cleaner in 6.5
    }


    @Before(order = 40)
    public void deleteAllDeployments() {
        flw.getAppRepositoryService().createDeploymentQuery().list().stream().map(AppDeployment::getId).forEach(id -> flw.getAppRepositoryService().deleteDeployment(id, true));
        flw.getRepositoryService().createDeploymentQuery().list().stream().map(Deployment::getId).forEach(id -> flw.getRepositoryService().deleteDeployment(id, true));
        flw.getCmmnRepositoryService().createDeploymentQuery().list().stream().map(CmmnDeployment::getId).forEach(id -> flw.getCmmnRepositoryService().deleteDeployment(id, true));
        flw.getDmnRepositoryService().createDeploymentQuery().list().stream().map(DmnDeployment::getId).forEach(id -> flw.getDmnRepositoryService().deleteDeployment(id));
        flw.getFormRepositoryService().createDeploymentQuery().list().stream().map(FormDeployment::getId).forEach(id -> flw.getFormRepositoryService().deleteDeployment(id));
    }

    // TODO: Other Engines
    // TODO: This doesn't work
    @Before(value = "@Mock", order = 50)
    public void useMockExpressionManager() {
        /*
        originalExpressionManager = ((ProcessEngineConfigurationImpl)flw.getProcessEngineConfiguration()).getExpressionManager();
        Mocks.reset();
        MockExpressionManager mockExpressionManager = new MockExpressionManager();
        ((ProcessEngineConfigurationImpl)flw.getProcessEngine().getProcessEngineConfiguration()).setExpressionManager(mockExpressionManager);
         */
    }


    @AfterStep(value = "@Async", order = 0)
    public void executeAsyncJobs() {
        JobTestHelper.waitForJobExecutorToProcessAllJobs(flw.getProcessEngineConfiguration(), flw.getManagementService(), asyncTimeout, asyncInterval, true);
    }



    @After(value = "@Mock", order = 10)
    public void resetExpressionManager() {
     //   ((ProcessEngineConfigurationImpl)flw.getProcessEngineConfiguration()).setExpressionManager(originalExpressionManager);
        Mocks.reset();
    }



}
