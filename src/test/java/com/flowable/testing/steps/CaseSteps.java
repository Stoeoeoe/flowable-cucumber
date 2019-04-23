package com.flowable.testing.steps;

import com.flowable.testing.config.CucumberTestConfiguration;
import com.flowable.testing.service.CaseTestService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnRepositoryService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.cmmn.api.repository.CaseDefinition;
import org.flowable.cmmn.api.repository.CmmnDeployment;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.IdentityService;
import org.flowable.identitylink.api.IdentityLinkType;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@ContextConfiguration(classes = {CucumberTestConfiguration.class})
public class CaseSteps {

    private final CmmnRuntimeService cmmnRuntimeService;
    private final CmmnRepositoryService cmmnRepositoryService;
    private  final CmmnHistoryService cmmnHistoryService;
    private final CaseTestService caseTestService;
    private final IdentityService identityService;
    private final CmmnTaskService cmmnTaskService;

    public CaseSteps(CmmnRuntimeService cmmnRuntimeService, CmmnRepositoryService cmmnRepositoryService, CmmnHistoryService cmmnHistoryService, CaseTestService caseTestService, IdentityService identityService, CmmnTaskService cmmnTaskService) {
        this.cmmnRuntimeService = cmmnRuntimeService;
        this.cmmnRepositoryService = cmmnRepositoryService;
        this.cmmnHistoryService = cmmnHistoryService;
        this.caseTestService = caseTestService;
        this.identityService = identityService;
        this.cmmnTaskService = cmmnTaskService;
    }

    @Given("^the case '(.*)' is deployed$")
    public void theCaseIsDeployed(String caseResource) throws Throwable {
        CmmnDeployment deployment = cmmnRepositoryService.createDeployment()
                .addClasspathResource(caseResource)
                .deploy();
        CaseDefinition caseDefinition = cmmnRepositoryService.createCaseDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        caseTestService.setCaseDefinition(caseDefinition);

        System.out.println("Case " + caseResource + " deployed as " + caseDefinition.getKey() + ".");
    }


    @Then("^the case is completed$")
    public void theProcessIsCompleted() throws Throwable {
        long completedProcesses = cmmnHistoryService.createHistoricCaseInstanceQuery()
                .caseInstanceId(caseTestService.getCaseInstance().getId())
                .finished()
                .count();
        if(completedProcesses == 0) {
            throw new FlowableException("The case was not completed successfully.");
        }
    }

    @Then("^the case is not yet completed$")
    public void theProcessIsNotYetCompleted() throws Throwable {
        long completedProcesses = cmmnHistoryService.createHistoricCaseInstanceQuery()
                .caseInstanceId(caseTestService.getCaseInstance().getId())
                .finished()
                .count();
        if(completedProcesses != 0) {
            throw new FlowableException("The case was already completed.");
        }
    }

    @When("^the case is started by '(.*)'$")
    public void theCaseIsStartedByAdmin(String starter) {
        CaseDefinition caseDefinition = caseTestService.getCaseDefinition();
        if(caseDefinition.hasStartFormKey()) {
            throw new FlowableException("The process " + caseDefinition.getKey() + " requires start form data.");
        }
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder()
                .caseDefinitionKey(caseDefinition.getKey())
                .start();
        caseTestService.setCaseInstance(caseInstance);

        // FIXME: Evaluate Starter dynamically
        cmmnRuntimeService.setVariable(caseInstance.getId(), "starter", starter);
        cmmnRuntimeService.addUserIdentityLink(caseInstance.getId(), starter, IdentityLinkType.STARTER);
    }

    @Then("^the plan item (.*) can be activated")
    public void thePlanItemCanBeActivated(String planItem) {
        List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
                .caseInstanceId(caseTestService.getCaseInstance().getId())
                .list();
        System.out.println(planItemInstances);
    }

    @Then("^the case '(.*)' can be started by '(.*)'$")
    public void theCaseCanBeStartedBy(String caseDefinitionKey, String user) {
        // TODO: Implement if possible
    }

    @When("^the human task '(.*)' is completed$")
    public void theHumanTaskTaskIsCompleted(String taskDefinitionKey) {
        String taskId = cmmnTaskService.createTaskQuery()
                .caseInstanceId(caseTestService.getCaseInstance().getId())
                .taskDefinitionKey(taskDefinitionKey)
                .singleResult()
                .getId();
        cmmnTaskService.complete(taskId);
    }
}
