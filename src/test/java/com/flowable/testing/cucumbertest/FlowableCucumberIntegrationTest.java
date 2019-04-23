package com.flowable.testing.cucumbertest;

import org.flowable.engine.test.ConfigurationResource;
import org.flowable.engine.test.FlowableTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/*
@ContextConfiguration(
        classes = SpringDemoApplication.class,
        loader = SpringApplicationContextLoader.class)
  */
@FlowableTest
@ConfigurationResource("cucumber.flowable.cfg.xml")
@SpringBootTest
public class FlowableCucumberIntegrationTest {

}
