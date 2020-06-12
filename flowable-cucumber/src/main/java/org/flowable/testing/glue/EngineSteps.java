package org.flowable.testing.glue;

import io.cucumber.java.en.Given;
import org.flowable.app.engine.AppEngine;
import org.flowable.cmmn.engine.CmmnEngine;
import org.flowable.dmn.engine.DmnEngine;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.form.engine.FormEngine;
import org.flowable.testing.service.FlowableServices;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Glue class responsible for setting up engines.
 */
public class EngineSteps {


    private final ProcessEngine processEngine;
    private final CmmnEngine cmmnEngine;
    private final AppEngine appEngine;
    private final DmnEngine dmnEngine;
    private final FormEngine formEngine;

    public EngineSteps(FlowableServices flowableServices) {
        this.processEngine = flowableServices.getProcessEngine();
        this.cmmnEngine = flowableServices.getCmmnEngine();
        this.appEngine = flowableServices.getAppEngine();
        this.dmnEngine = flowableServices.getDmnEngine();
        this.formEngine = flowableServices.getFormEngine();
    }

    // TODO: This doesn't really do anything :)...
    @Given("a Process Engine is running")
    public void anInMemoryProcessEngineIsRunning() {
        assertThat(processEngine).as("Process Engine is not running").isNotNull();
    }

    // TODO: Not done yet. Is this really needed?
    @Given("a Process Engine with the configuration file located at {string} is running")
    public void aProcessEngineWithTheConfigurationFileLocatedAtIsRunning(String configurationLocated) {
//        applicationContext.getBean("processEngine");
        ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(configurationLocated);
    }

    @Given("a CMMN engine is running")
    public void anInMemoryCmmnEngineIsRunning() {
        assertThat(cmmnEngine).as("CMMN Engine is not running").isNotNull();
    }

    @Given("an App engine is running")
    public void anInMemoryAppEngineIsRunning() {
        assertThat(appEngine).as("App Engine is not running").isNotNull();
    }

    @Given("a DMN engine is running")
    public void anInMemoryDmnEngineIsRunning() {
        assertThat(dmnEngine).as("DMN Engine is not running").isNotNull();
    }

    @Given("a Form engine is running")
    public void anInMemoryFormEngineIsRunning() {
        assertThat(formEngine).as("Form Engine is not running").isNotNull();
    }
/*
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

 */
}
