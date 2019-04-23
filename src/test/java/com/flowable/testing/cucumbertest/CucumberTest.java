package com.flowable.testing.cucumbertest;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.flowable.engine.test.FlowableTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", snippets = SnippetType.CAMELCASE, glue = "com.flowable.testing")
public class CucumberTest {

}
