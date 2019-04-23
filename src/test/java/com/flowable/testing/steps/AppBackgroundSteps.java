package com.flowable.testing.steps;

import com.flowable.testing.config.CucumberTestConfiguration;
import cucumber.api.java.en.Given;
import org.flowable.app.engine.AppEngine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

@ContextConfiguration(classes = {CucumberTestConfiguration.class})
public class AppBackgroundSteps {

    private final AppEngine appEngine;

    public AppBackgroundSteps(AppEngine appEngine) {
        this.appEngine = appEngine;
    }

    @Given("^an in-memory app engine is running$")
    public void anInMemoryAppEngineIsRunning() {
        Assert.notNull(appEngine, "App Engine is not running");
    }
}
