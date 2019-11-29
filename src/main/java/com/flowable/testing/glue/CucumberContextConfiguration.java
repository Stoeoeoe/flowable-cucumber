package com.flowable.testing.glue;

import org.springframework.boot.test.context.SpringBootTest;

import com.flowable.testing.FlowableCucumberDemoApplication;
import com.flowable.testing.config.CucumberTestConfiguration;

import io.cucumber.java.Before;

@SpringBootTest(classes = { FlowableCucumberDemoApplication.class, CucumberTestConfiguration.class })
public class CucumberContextConfiguration  {

    @Before
    public void SetupCucumberSpringContext(){
        // Dummy method so cucumber will recognize this class as glue
        // and use its context configuration.
    }
}