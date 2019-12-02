package org.flowable.testing.glue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.IdentityService;
import org.flowable.engine.TaskService;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.idm.api.Group;
import org.flowable.task.api.Task;
import org.flowable.testing.service.CucumberProcessTestService;
import org.flowable.testing.util.CucumberVariableUtils;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java8.En;

// TODO: Check if historic needed
public class TaskSteps implements En {

    private final TaskService taskService;
    private final CucumberProcessTestService cucumberProcessTestService;
    private final FormRepositoryService formRepositoryService;
    private final IdentityService identityService;

    public TaskSteps(TaskService taskService, CucumberProcessTestService cucumberProcessTestService,
        FormRepositoryService formRepositoryService, IdentityService identityService) {
        this.taskService = taskService;
        this.cucumberProcessTestService = cucumberProcessTestService;
        this.formRepositoryService = formRepositoryService;
        this.identityService = identityService;
    }

    @And("the user task {string} is completed")
    public void theTaskIsCompleted(String taskDefinitionKey) {
        Task task = taskService.createTaskQuery().taskDefinitionKey(taskDefinitionKey).singleResult();
        if(task == null) {
            throw new FlowableException("Task with the key " + taskDefinitionKey + " does not exist.");
        }
        taskService.complete(task.getId());
    }

    @And("the user task {string} is completed with the following form data:")
    public void theTaskIsCompletedWithTheFollowingFormData(String taskDefinitionKey, DataTable variables)  throws Throwable{
        theUserTaskIsCompletedWithTheOutcomeAndTheFollowingFormData(taskDefinitionKey, null, variables);
    }

    @And("the user task {string} is completed with the outcome {string} and the following form data:")
    public void theUserTaskIsCompletedWithTheOutcomeAndTheFollowingFormData(String taskDefinitionKey, String outcome, DataTable variables) {
        Task task =
            taskService.createTaskQuery().
                taskDefinitionKey(taskDefinitionKey)
                .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
                .singleResult();
        if(task == null) {
            throw new FlowableException("Task with the key " + taskDefinitionKey + " does not exist.");
        }
        String formDefinitionId = formRepositoryService.createFormDefinitionQuery()
            .formDefinitionKey(task.getFormKey())
            .latestVersion()
            .singleResult()
            .getId();

        Map<String, Object> variableMap = CucumberVariableUtils.getMapFromDataTable(variables);
        taskService.completeTaskWithForm(task.getId(), formDefinitionId, outcome, variableMap, false);
    }


    @When("the user/human task {string} is claimed by {string}")
    public void theUserTaskAccountingTaskIsClaimedBy(String taskDefinitionKey, String userId) {
        String taskId = taskService.createTaskQuery()
            .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
            .taskDefinitionKey(taskDefinitionKey)
            .singleResult()
            .getId();
        taskService.claim(taskId, userId);
    }

    @Then("the user {string} is {string} of the user task {string}")
    public void theUserHasIdentityLinkOnTask(String userId, String identityLinkType, String taskDefinitionKey) {
        checkNumberOfIdentityLinksOnTask(userId, identityLinkType, taskDefinitionKey, 1);
    }

    @Then("the user {string} is not {string} of the user task {string}")
    public void theUserDoesNotHaveIdentityLinkOnTask(String userId, String identityLinkType, String taskDefinitionKey) {
        checkNumberOfIdentityLinksOnTask(userId, identityLinkType, taskDefinitionKey, 0);
    }

    private void checkNumberOfIdentityLinksOnTask(String userId, String identityLinkType, String taskDefinitionKey, int minNumberOfLinks) {
        Task task = taskService.createTaskQuery()
            .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
            .taskDefinitionKey(taskDefinitionKey)
            .includeIdentityLinks()
            .singleResult();

        if(task == null) {
            throw new FlowableException("Task with the key " + taskDefinitionKey + " does not exist.");
        }

        List<String> userGroupIds = identityService.createGroupQuery()
            .groupMember(userId)
            .list()
            .stream()
            .map(Group::getId)
            .collect(Collectors.toList());

        boolean hasUserIdentityLinks = task.getIdentityLinks().stream()
            .filter(il -> il.getType().equals(identityLinkType))
            .filter(il -> userId.equals(il.getUserId()) ||  userGroupIds.contains(il.getGroupId()) )
            .count() >= minNumberOfLinks;


        if(!hasUserIdentityLinks) {
            throw new RuntimeException("user '" + userId + "' does not have an identity link of type '" + identityLinkType + "' on the task " + taskDefinitionKey);
        }

    }
}
