package org.flowable.testing.glue;

//import org.flowable.testing.FlowableCucumberDemoApplication;
import org.flowable.testing.config.CucumberTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.Before;

//@SpringBootTest(classes = { FlowableCucumberDemoApplication.class, CucumberTestConfiguration.class })
@SpringBootTest(classes = { CucumberTestConfiguration.class })
public class CucumberContextConfiguration {

    @Before
    public void SetupCucumberSpringContext(){
        // Dummy method so cucumber will recognize this class as glue and use its context configuration.
    }
}