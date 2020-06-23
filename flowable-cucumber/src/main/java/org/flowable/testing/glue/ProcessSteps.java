package org.flowable.testing.glue;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.test.JobTestHelper;
import org.flowable.engine.migration.ActivityMigrationMapping;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.job.api.Job;
import org.flowable.testing.service.CucumberProcessTestService;
import org.flowable.testing.service.FlowableServices;
import org.flowable.testing.util.CucumberVariableUtils;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.core.io.ClassPathResource;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.*;
import static org.flowable.testing.util.FlowableCucumberConstants.ENGINE_PROCESS;
import static org.junit.Assert.assertTrue;

public class ProcessSteps {

//    Logger logger = Logger.getLogger(ProcessSteps.class.getName());

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final IdentityService identityService;
    private final FormRepositoryService formRepositoryService;
    private final CucumberProcessTestService cucumberProcessTestService;
    private final ProcessEngineConfiguration processEngineConfiguration;
    private final ManagementService managementService;
    private final ProcessMigrationService processMigrationService;

    public ProcessSteps(FlowableServices flowableServices, CucumberProcessTestService cucumberProcessTestService) {
        this.cucumberProcessTestService = cucumberProcessTestService;

        this.runtimeService = flowableServices.getRuntimeService();
        this.repositoryService = flowableServices.getRepositoryService();
        this.historyService = flowableServices.getHistoryService();
        this.identityService = flowableServices.getIdentityService();
        this.formRepositoryService = flowableServices.getFormRepositoryService();
        this.processEngineConfiguration = flowableServices.getProcessEngineConfiguration();
        this.managementService = flowableServices.getManagementService();
        this.processMigrationService = flowableServices.getProcessMigrationService();
    }

    @Given("the process {string} is deployed")
    public void theProcessIsDeployed(String processResource) throws Throwable {
        theProcessIsDeployedAs(processResource, null);
    }

    @Given("the process {string} is deployed as {string}")
    public void theProcessIsDeployedAs(String processResource, String key) throws Throwable {
        ClassPathResource resource = new ClassPathResource(processResource);
        if(!resource.exists()) {
            throw new IllegalArgumentException("There is no process model " + processResource + " ");
        }

        Deployment deployment = repositoryService.createDeployment()
            .addClasspathResource(processResource)
            .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        if(!cucumberProcessTestService.hasDefinition(ENGINE_PROCESS, processDefinition.getKey())) {
            String definitionKey = key != null? key : processDefinition.getKey();
            cucumberProcessTestService.addDefinition(ENGINE_PROCESS, definitionKey, processDefinition);
          //  logger.info("Process " + processResource + " deployed as " + definitionKey );
        }
    }

    @When("the process {string} is started by {string} with tokens on the following activities:")
    public void thereIsATokenOnTheFlowElement(String processDefinitionKey, String starter, DataTable dataTable) {
        List<String> activityIds = dataTable.asList();
        boolean asyncExecutorActive = processEngineConfiguration.getAsyncExecutor().isActive();

        // Deactivate async executor to make sure that the token remains on the start event.
        if(asyncExecutorActive) {
            processEngineConfiguration.setAsyncExecutorActivate(false);
        }

        identityService.setAuthenticatedUserId(starter);
        ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
            .processDefinitionKey(processDefinitionKey)
            .startAsync();

        String startActivityId = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).singleResult().getActivityId();
        ActivityMigrationMapping.OneToManyMapping mapping = new ActivityMigrationMapping.OneToManyMapping(startActivityId, activityIds);

        processMigrationService.createProcessInstanceMigrationBuilder()
            .addActivityMigrationMapping(mapping)
            .migrate(processInstance.getId());

        // Start async executor again if the process is to be start asynchronously.
        if(asyncExecutorActive) {
            processEngineConfiguration.setAsyncExecutorActivate(true);
        }

        cucumberProcessTestService.setProcessInstance(processInstance);
    }

    @Given("the current date is {string}")
    public void theCurrentDateIs(String dateString) throws Throwable {
        LocalDate localDate = LocalDate.parse(dateString);
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        processEngineConfiguration.getClock().setCurrentTime(date);
    }


    @Given("the current date is {string} and the time is {string}")
    public void theCurrentDateAndTimeIs(String dateString, String timeString) throws Throwable {
        LocalDate localDate = LocalDate.parse(dateString);
        LocalTime localTime = LocalTime.parse(timeString);
        LocalDateTime localDateTime = localDate.atTime(localTime);
        Date date = Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant());
        processEngineConfiguration.getClock().setCurrentTime(date);
    }

    @When("the process {string} is started by {string}")
    public void theProcessIsStartedBy(String processDefinitionKey, String starter) {
        identityService.setAuthenticatedUserId(starter);
        theProcessIsStarted(processDefinitionKey);
    }

    @When("the process {string} is started")
    public void theProcessIsStarted(String processDefinitionKey) {
        ProcessDefinition processDefinition = cucumberProcessTestService.getDefinition(ENGINE_PROCESS, processDefinitionKey);
        if(processDefinition.hasStartFormKey()) {
            throw new FlowableException("The process " + processDefinition.getKey() + " requires start form data.");
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        cucumberProcessTestService.setProcessInstance(processInstance);
    }



    @Then("the process is completed")
    public void theProcessIsCompleted() {
        long completedProcesses = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
                .finished()
                .count();
        if(completedProcesses == 0) {
            throw new FlowableException("The process was not completed yet.");
        }
    }

    @Then("the process is not yet completed")
    public void theProcessIsNotYetCompleted() {
        long completedProcesses = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
                .finished()
                .count();
        if(completedProcesses != 0) {
            throw new FlowableException("The process was already completed.");
        }
    }

    @Then("the activity {string} is active")
    public void theActivityIsActive(String activityId) throws Throwable {
        thereAreActiveInstancesWithTheId(1, activityId);
    }

    @And("the activity {string} is not active")
    public void theActivitySecondUserTaskIsNotAnymoreActive(String activityId) throws Throwable{
        thereAreActiveInstancesWithTheId(0, activityId);
    }


    @Then("there are {biginteger} active instances with the ID {string}")
    public void thereAreActiveInstancesWithTheId(long count, String activityId) {
        long actualCount = runtimeService.createActivityInstanceQuery()
            .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
            .activityId(activityId)
            .count();

        if(count != actualCount) {
            throw new RuntimeException("There are " + actualCount + " activities with the ID " + activityId + " instead of " + count);
        }
    }


    @And("the process variables are as follows:")
    public void theProcessVariablesAreAsFollows( DataTable variables) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
                .includeProcessVariables()
                .singleResult();
        Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
        Map<String, Object> variableMap = CucumberVariableUtils.getMapFromDataTable(variables);

        for (Map.Entry<String, Object> entry: variableMap.entrySet()) {
            String variableName = entry.getKey();
            Object expectedValue = entry.getValue();

            if(!processVariables.containsKey(variableName)) {
                throw new FlowableException("Variable " + variableName + " does not exist.");
            } else if(!processVariables.get(variableName).equals(expectedValue)) {
                throw new FlowableException("Expected variable value for '" + variableName + "' is '" + processVariables.get(variableName) + "' instead of '" + expectedValue + "'");
            }
        }
    }

    @Then("the process {string} can be started by {string}")
    public void theProcessCanBeStartedBy(String processDefinitionKey, String user) {
        ProcessDefinition processDefinition = cucumberProcessTestService.getDefinition(ENGINE_PROCESS, processDefinitionKey);
        if(processDefinition == null) {
            throw new RuntimeException("There is no process definition with the key '" + processDefinitionKey + "'");
        }
        boolean userCanStartProcess = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(processDefinition.getId())
            .startableByUser(user)
            .latestVersion()
            .count() > 0;
        assertTrue("User cannot start process", userCanStartProcess);
    }


   @When("the global signal {string} is thrown")
   public void theGlobalSignalWasThrown(String signalName) {
        runtimeService.signalEventReceived(signalName);
   }

    @When("the global signal {string} with the following payload is thrown")
    public void theGlobalSignalWithTheFollowingPayloadWasThrown(String signalName, DataTable dataTable) {
        Map<String, Object> variableMap = CucumberVariableUtils.getMapFromDataTable(dataTable);
        runtimeService.signalEventReceived(signalName, variableMap);
    }

    @When("the local signal {string} is thrown")
    public void theLocalSignalWasThrown(String signalName) {
        theLocalSignalWithTheFollowingPayloadWasThrown(signalName, DataTable.emptyDataTable());

    }

    @When("the local signal {string} with the following payload is thrown")
    public void theLocalSignalWithTheFollowingPayloadWasThrown(String signalName, DataTable dataTable) {
        Map<String, Object> variableMap = CucumberVariableUtils.getMapFromDataTable(dataTable);
        List<String> childExecutionIds = runtimeService.createExecutionQuery()
            .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
            .signalEventSubscriptionName(signalName)
            .list()
            .stream()
            .map(Execution::getId)
            .collect(Collectors.toList());
        for (String childExecutionId : childExecutionIds) {
            runtimeService.signalEventReceived(signalName, childExecutionId, variableMap);
        }
    }



    @When("the message {string} is received")
    public void theMessageWasReceived(String messageName) {
        theMessageWasReceivedWithPayload(messageName, DataTable.emptyDataTable());
    }
    
    @When("the message {string} is received with the following payload:")
    public void theMessageWasReceivedWithPayload(String messageName, DataTable dataTable) {
        Map<String, Object> variableMap = CucumberVariableUtils.getMapFromDataTable(dataTable);

        List<String> childExecutionIds = runtimeService.createExecutionQuery()
            .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
            .messageEventSubscriptionName(messageName)
            .list()
            .stream()
            .map(Execution::getId)
            .collect(Collectors.toList());
        for (String childExecutionId : childExecutionIds) {
            runtimeService.messageEventReceived(messageName, childExecutionId, variableMap);
        }
    }

    @When("the activity {string} is triggered")
    public void theActivityIsTriggered(String activityName) {
        triggerActivity(activityName, DataTable.emptyDataTable(), false);
    }

    // TODO: Fix
    @When("the activity {string} is triggered asynchronously")
    public void theActivityIsTriggeredAsynchronously(String activityName) {
        triggerActivity(activityName, DataTable.emptyDataTable(), true);
    }

    @When("the activity {string} is triggered with the following payload:")
    public void theActivityIsTriggeredWithTheFollowingPayload(String activityName, DataTable dataTable) {
        triggerActivity(activityName, dataTable, false);
    }

    @When("the activity {string} is triggered asynchronously with the following payload:")
    public void theActivityIsTriggeredAsynchronouslyWithTheFollowingPayload(String activityName, DataTable dataTable) {
        triggerActivity(activityName, dataTable, true);
    }

    private void triggerActivity(String activityName, DataTable dataTable, boolean async) {
        Map<String, Object> payload = CucumberVariableUtils.getMapFromDataTable(dataTable);
        List<String> executionIds = runtimeService.createExecutionQuery()
            .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
            .activityId(activityName)
            .list()
            .stream()
            .map(Execution::getId)
            .collect(Collectors.toList());
        for (String executionId : executionIds) {
            if(async) {
               runtimeService.triggerAsync(executionId, payload);
               JobTestHelper.waitForJobExecutorToProcessAllJobs(processEngineConfiguration, managementService, 10000, 100, true);
            } else {
                runtimeService.trigger(executionId, payload);
            }
        }

    }



    @When("{biginteger} seconds passed")
    public void secondsPassed(BigInteger seconds) {
        adjustClock(seconds, SECONDS);
    }

    @When("{biginteger} minutes passed")
    public void minutesPassed(BigInteger minutes) {
        adjustClock(minutes, MINUTES);
    }

    @When("{biginteger} hours passed")
    public void hoursPassed(BigInteger hours) {
        adjustClock(hours, HOURS);
    }

    @When("{biginteger} days passed")
    public void daysPassed(BigInteger days) {
        adjustClock(days, DAYS);
    }


    private void adjustClock(BigInteger amount, ChronoUnit chronoUnit) {
        Date currentTime = processEngineConfiguration.getClock().getCurrentTime();
        Date newTime = Date.from(currentTime.toInstant().plus(amount.longValue(), chronoUnit));
        processEngineConfiguration.getClock().setCurrentTime(newTime);

        JobTestHelper.waitForJobExecutorToProcessAllJobsAndExecutableTimerJobs(processEngineConfiguration, managementService, 10000, 100, true);
    }

    @Then("the process variable {string} is {string}")
    public void theProcessVariableValueIs(String variableName, String expectedStringValue) {
        // TODO: Maybe there's a way to work with types here? Right now, the string values are compared
        Object variableStringValue = getVariableValue(variableName).toString();
        if(!expectedStringValue.equals(variableStringValue)) {
            throw new RuntimeException("The variable '" + variableName + "' does not have the expected value '" + expectedStringValue + "', instead the value is '" + variableStringValue + "'");
        }
    }

    @Then("the process variable {string} is not {string}")
    public void theProcessVariableIsNot(String variableName, String variableValueNotExpected) {
        Object variableStringValue = getVariableValue(variableName);
        if(variableValueNotExpected.equals(variableStringValue)) {
            throw new RuntimeException("The variable '" + variableName + "' is unexpectadly equals to '" + variableValueNotExpected + "'");
        }
    }

    @Then("the process variable {string} does not exist")
    public void theProcessVariableDoesNotExist(String variableName) {
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
            .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
            .variableName(variableName)
            .singleResult();
        if(historicVariableInstance != null) {
            throw new RuntimeException("The variable " + variableName + " does exist and has the value " + historicVariableInstance.getValue());
        }
    }

    private Object getVariableValue(String variableName) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
            .includeProcessVariables()
            .singleResult();
        Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
        if (!processVariables.containsKey(variableName)) {
            throw new RuntimeException("There is no variable with the name '" + variableName + "' stored on the process.");
        }
        return processVariables.get(variableName).toString();
    }

    @When("the async service task {string} is executed")
    public void theAsyncServiceTaskServiceTaskIsExecuted(String activityId) {
        theAsyncActivityIsExecuted(activityId);
    }

    @Then("the async activity {string} is executed")
    public void theAsyncActivityIsExecuted(String activityId) {
        List<Execution> executions = runtimeService.createExecutionQuery()
            .activityId(activityId)
            .list();
        for (Execution execution : executions) {
            List<Job> jobs = managementService.createJobQuery()
                .processInstanceId(cucumberProcessTestService.getProcessInstanceId())
                .executionId(execution.getId())
                .list();

            for (Job job : jobs) {
                managementService.executeJob(job.getId());
            }
        }
    }

    @When("all async jobs are executed")
    public void allAsyncJobsAreExecuted() {
        JobTestHelper.waitForJobExecutorToProcessAllJobsAndExecutableTimerJobs(processEngineConfiguration, managementService, 10000, 100, true);
    }


    // TODO: This is the only place where we have support for multiple processes - do we need more of that?
    @Then("there is/are {int} process(es) with the definition key {string}")
    public void thereIsProcessWithTheDefinitionKeyGlobal_signal_process(int numberOfProcesses, String processDefinitionKey) {
        long count = runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).count();
        if(count != numberOfProcesses) {
            throw new RuntimeException("There are " + count + " running processes.");
        }
    }


    @When("the process variables are changed to:")
    public void theProcessVariablesAreChangedTo(DataTable variables) {
        Map<String, Object> variableMap = CucumberVariableUtils.getMapFromDataTable(variables);
        runtimeService.setVariables(cucumberProcessTestService.getProcessInstanceId(), variableMap);
    }
}
