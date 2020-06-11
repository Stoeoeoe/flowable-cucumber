package org.flowable.testing.glue;

import java.util.logging.Logger;

import org.flowable.app.engine.AppEngine;
import org.flowable.cmmn.engine.CmmnEngine;
import org.flowable.dmn.engine.DmnEngine;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.form.engine.FormEngine;
import static org.assertj.core.api.Assertions.assertThat;
/*
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
*/

import io.cucumber.java.en.Given;
import sun.jvm.hotspot.utilities.Assert;

/**
 * Glue class responsible for setting up engines.
 */
public class EngineSteps {

    private final ProcessEngine processEngine;
    private final CmmnEngine cmmnEngine;
    private final AppEngine appEngine;
    private final DmnEngine dmnEngine;
    private final FormEngine formEngine;

//    private ApplicationContext applicationContext;

    public EngineSteps(ProcessEngine processEngine, AppEngine appEngine, CmmnEngine cmmnEngine, DmnEngine dmnEngine, FormEngine formEngine) {
        this.processEngine = processEngine;
        this.cmmnEngine = cmmnEngine;
        this.appEngine = appEngine;
        this.dmnEngine = dmnEngine;
        this.formEngine = formEngine;
    }

    // TODO: This doesn't really do anything :)...
    @Given("an Process Engine is running")
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
