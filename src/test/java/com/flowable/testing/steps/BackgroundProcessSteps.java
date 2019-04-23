package com.flowable.testing.steps;


import com.flowable.testing.config.CucumberTestConfiguration;
import cucumber.api.java.en.Given;
import org.flowable.engine.ProcessEngine;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CucumberTestConfiguration.class})
public class BackgroundProcessSteps  {

    private final ProcessEngine processEngine;

    public BackgroundProcessSteps(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Given("^an in-memory process engine is running$")
    public void anInMemoryProcessEngineIsRunning()  throws Throwable {
        Assert.assertNotNull(processEngine);
    }
}
