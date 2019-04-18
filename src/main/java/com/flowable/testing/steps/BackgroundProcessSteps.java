package com.flowable.testing.steps;


import cucumber.api.java.en.Given;

public class BackgroundProcessSteps {

    @Given("^an in-memory process engine is running$")
    public void anInMemoryProcessEngineIsRunning() {
        System.out.println("Created process engine");
    }
}
