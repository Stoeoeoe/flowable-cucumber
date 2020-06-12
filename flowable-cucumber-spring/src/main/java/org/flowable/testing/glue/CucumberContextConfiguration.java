package org.flowable.testing.glue;

//import org.flowable.testing.FlowableCucumberDemoApplication;
import org.flowable.testing.config.CucumberTestConfiguration;
import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest;


//@SpringBootTest(classes = { FlowableCucumberDemoApplication.class, CucumberTestConfiguration.class })
@io.cucumber.spring.CucumberContextConfiguration
@SpringBootTest(classes = CucumberTestConfiguration.class)
public class CucumberContextConfiguration {

    @Before
    public void SetupCucumberSpringContext(){
        // Dummy method so cucumber will recognize this class as glue and use its context configuration.
    }
}