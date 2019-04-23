package com.flowable.testing.steps;

import com.flowable.testing.config.CucumberTestConfiguration;
import cucumber.api.java.en.Given;
import org.flowable.cmmn.engine.CmmnEngine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

@ContextConfiguration(classes = {CucumberTestConfiguration.class})
public class CaseBackgroundSteps {

    private final CmmnEngine cmmnEngine;

    public CaseBackgroundSteps(CmmnEngine cmmnEngine) {
        this.cmmnEngine = cmmnEngine;
    }

    @Given("^an in-memory CMMN engine is running$")
    public void anInMemoryCMMNEngineIsRunning() {
        Assert.notNull(cmmnEngine, "Case Engine is not initialized");
    }
}
