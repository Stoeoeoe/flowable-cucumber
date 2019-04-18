package com.flowable.testing.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Component;

@Component
public class ProcessSteps {

    private final RuntimeService runtimeService;

    public ProcessSteps(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }



    @Given("^the process '(.*)' is deployed$")
    public void theProcessSimple_processIsDeployed(String processDefinitionKey) {
        System.out.println("Process " + processDefinitionKey + " deployed.");
    }

    @Then("^the process is started$")
    public void theProcessIsStarted() {
        System.out.println("Process started.");
    }

    @And("^the user task '(.*)' was filled as follows:$")
    public void theUserTaskTaskWasFilledAsFollows(String taskDefinitionKey, DataTable dataTable) {
        System.out.println("Form filled.");
    }

}
