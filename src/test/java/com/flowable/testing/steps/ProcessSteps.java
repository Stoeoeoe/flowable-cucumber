package com.flowable.testing.steps;

import com.flowable.testing.config.CucumberTestConfiguration;
import com.flowable.testing.service.ProcessTestService;
import com.flowable.testing.util.CucumberVariableUtils;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ContextConfiguration(classes = {CucumberTestConfiguration.class})
public class ProcessSteps {

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final IdentityService identityService;
    private final FormRepositoryService formRepositoryService;

    private final ProcessTestService processTestService;

    public ProcessSteps(RuntimeService runtimeService, RepositoryService repositoryService, TaskService taskService, HistoryService historyService, IdentityService identityService, FormRepositoryService formRepositoryService, ProcessTestService processTestService) {
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
        this.taskService = taskService;
        this.historyService = historyService;
        this.identityService = identityService;
        this.formRepositoryService = formRepositoryService;
        this.processTestService = processTestService;
    }

    @Given("^the process '(.*)' is deployed$")
    public void theProcessIsDeployed(String processResource) throws Throwable {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(processResource)
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        processTestService.setProcessDefinition(processDefinition);

        System.out.println("Process " + processResource + " deployed as " + processDefinition.getKey() + ".");
    }

    @When("^the process is started by '(.*)'$")
    public void theProcessIsStartedBy(String starter) {
        ProcessDefinition processDefinition = processTestService.getProcessDefinition();
        if(processDefinition.hasStartFormKey()) {
            throw new FlowableException("The process " + processDefinition.getKey() + " requires start form data.");
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        processTestService.setProcessInstance(processInstance);

        // FIXME: Evaluate Starter dynamically
        runtimeService.setVariable(processInstance.getId(), "starter", starter);
        runtimeService.addUserIdentityLink(processInstance.getId(), starter, IdentityLinkType.STARTER);
    }

    @And("^the user task '(.*)' is completed$")
    public void theTaskIsCompleted(String taskDefinitionKey) {
        Task task = taskService.createTaskQuery().taskDefinitionKey(taskDefinitionKey).singleResult();
        if(task == null) {
            throw new FlowableException("Task with the key " + taskDefinitionKey + " does not exist.");
        }
        taskService.complete(task.getId());
    }

    @And("^the user task '(.*)' is completed with the following form data:$")
    public void theTaskIsCompletedWithTheFollowingFormData(String taskDefinitionKey, DataTable variables)  throws Throwable{
        theUserTaskIsCompletedWithTheOutcomeAndTheFollowingFormData(taskDefinitionKey, null, variables);
    }

    @And("^the user task '(.*)' is completed with the outcome '(.*)' and the following form data:$")
    public void theUserTaskIsCompletedWithTheOutcomeAndTheFollowingFormData(String taskDefinitionKey, String outcome, DataTable variables) {
        Task task =
                taskService.createTaskQuery().
                        taskDefinitionKey(taskDefinitionKey)
                        .processInstanceId(processTestService.getProcessInstance().getId())
                        .singleResult();
        if(task == null) {
            throw new FlowableException("Task with the key " + taskDefinitionKey + " does not exist.");
        }
        String formDefinitionId = formRepositoryService.createFormDefinitionQuery()
                .formDefinitionKey(task.getFormKey())
                .latestVersion()
                .singleResult()
                .getId();

        Map<String, Object> variableMap = new HashMap<>();
        for (DataTableRow row : variables.getGherkinRows()) {
            List<String> cells = row.getCells();
            Object value = CucumberVariableUtils.parseDataTableVariable(cells.get(1), cells.get(2));
            variableMap.put(cells.get(0), value);
        }

        taskService.completeTaskWithForm(task.getId(), formDefinitionId, outcome, variableMap, false);
        System.out.println("Form filled.");
    }



    @Then("^the process is completed$")
    public void theProcessIsCompleted() throws Throwable {
        long completedProcesses = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processTestService.getProcessInstance().getId())
                .finished()
                .count();
        if(completedProcesses == 0) {
            throw new FlowableException("The process was not completed successfully.");
        }
    }

    @Then("^the process is not yet completed$")
    public void theProcessIsNotYetCompleted() throws Throwable {
        long completedProcesses = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processTestService.getProcessInstance().getId())
                .finished()
                .count();
        if(completedProcesses != 0) {
            throw new FlowableException("The process was already completed.");
        }
    }


    @And("^the process variables are as follows:$")
    public void theProcessVariablesAreAsFollows( DataTable variables) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processTestService.getProcessInstance().getId())
                .includeProcessVariables()
                .singleResult();
        Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();

        for (DataTableRow row : variables.getGherkinRows()) {
            List<String> cells = row.getCells();
            String variableName = cells.get(0);
            Object expectedValue = CucumberVariableUtils.parseDataTableVariable(cells.get(1), cells.get(2));
            if(!processVariables.containsKey(variableName)) {
                throw new FlowableException("Variable " + variableName + " does not exist.");
            } else if(!processVariables.get(variableName).equals(expectedValue)) {
                throw new FlowableException("Expected variable value for '" + variableName + "' is '" + processVariables.get(variableName) + "' instead of '" + expectedValue + "'");
            }
        }
    }

    // TODO: Check if reference to process is necessary or if there can only ever be one process...
    @Then("^the process '(.*)' can be started by '(.*)'$")
    public void theProcessCanBeStartedBy(String processDefinitionKey, String user) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).singleResult();
        List<User> potentialStarterUsers = identityService.getPotentialStarterUsers(processDefinition.getId());

        List<Group> potentialStarterGroups = identityService.getPotentialStarterGroups(processTestService.getProcessDefinition().getId());
        List<User> groupUsers = identityService.createUserQuery()
                .memberOfGroups(potentialStarterGroups.stream().map(g -> g.getId()).collect(Collectors.toList()))
                .list();
        potentialStarterUsers.addAll(groupUsers);
        boolean containsUser = potentialStarterUsers.stream().map(User::getId).anyMatch(u -> u.equals(user));
        Assert.assertTrue("User cannot start process", containsUser);
    }
}
