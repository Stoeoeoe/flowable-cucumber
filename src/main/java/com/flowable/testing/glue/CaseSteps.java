package com.flowable.testing.glue;

import com.flowable.testing.service.CucumberCaseTestService;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnRepositoryService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.cmmn.api.repository.CaseDefinition;
import org.flowable.cmmn.api.repository.CmmnDeployment;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.flowable.cmmn.model.CmmnModel;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.IdentityService;
import org.flowable.identitylink.api.IdentityLinkType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.logging.Logger;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CaseSteps {

    Logger logger = Logger.getLogger(CaseSteps.class.getName());

    @Autowired
    private CmmnRuntimeService cmmnRuntimeService;
    @Autowired
    private CmmnRepositoryService cmmnRepositoryService;
    @Autowired
    private CmmnHistoryService cmmnHistoryService;
    @Autowired
    private CucumberCaseTestService cucumberCaseTestService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private CmmnTaskService cmmnTaskService;


    @Given("the case {string} is deployed")
    public void theCaseIsDeployed(String caseResource) throws Throwable {
        CmmnDeployment deployment = cmmnRepositoryService.createDeployment()
                .addClasspathResource(caseResource)
                .deploy();
        CaseDefinition caseDefinition = cmmnRepositoryService.createCaseDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        cucumberCaseTestService.setCaseDefinition(caseDefinition);

        System.out.println("Case " + caseResource + " deployed as " + caseDefinition.getKey() + ".");
    }


    @Then("the case is completed")
    public void theProcessIsCompleted() throws Throwable {
        long completedProcesses = cmmnHistoryService.createHistoricCaseInstanceQuery()
                .caseInstanceId(cucumberCaseTestService.getCaseInstance().getId())
                .finished()
                .count();
        if(completedProcesses == 0) {
            throw new FlowableException("The case was not completed successfully.");
        }
    }

    @Then("the case is not yet completed")
    public void theProcessIsNotYetCompleted() throws Throwable {
        long completedProcesses = cmmnHistoryService.createHistoricCaseInstanceQuery()
                .caseInstanceId(cucumberCaseTestService.getCaseInstance().getId())
                .finished()
                .count();
        if(completedProcesses != 0) {
            throw new FlowableException("The case was already completed.");
        }
    }

    @When("the case is started by {string}")
    public void theCaseIsStartedByAdmin(String starter) {
        CaseDefinition caseDefinition = cucumberCaseTestService.getCaseDefinition();
        if(caseDefinition.hasStartFormKey()) {
            throw new FlowableException("The process " + caseDefinition.getKey() + " requires start form data.");
        }
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder()
                .caseDefinitionKey(caseDefinition.getKey())
                .start();
        cucumberCaseTestService.setCaseInstance(caseInstance);

        // FIXME: Evaluate Starter dynamically
        cmmnRuntimeService.setVariable(caseInstance.getId(), "starter", starter);
        cmmnRuntimeService.addUserIdentityLink(caseInstance.getId(), starter, IdentityLinkType.STARTER);
    }

    @Then("the plan item {string} can be activated")
    public void thePlanItemCanBeActivated(String planItem) {
        List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
                .caseInstanceId(cucumberCaseTestService.getCaseInstance().getId())
                .list();
    }

    @Then("the case {string} can be started by {string}")
    public void theCaseCanBeStartedBy(String caseDefinitionKey, String user) {
//        identityService.setAuthenticatedUserId(user);
        CaseDefinition caseDefinition = cmmnRepositoryService.createCaseDefinitionQuery()
            .caseDefinitionKey(caseDefinitionKey)
            .singleResult();
        CmmnModel cmmnModel = cmmnRepositoryService.getCmmnModel(caseDefinition.getId());


    }

    @When("the human task {string} is completed")
    public void theHumanTaskTaskIsCompleted(String taskDefinitionKey) {
        String taskId = cmmnTaskService.createTaskQuery()
                .caseInstanceId(cucumberCaseTestService.getCaseInstance().getId())
                .taskDefinitionKey(taskDefinitionKey)
                .singleResult()
                .getId();
        cmmnTaskService.complete(taskId);
    }
}
