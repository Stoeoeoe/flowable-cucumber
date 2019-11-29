package com.flowable.testing;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features/",
    plugin = {"pretty"},
    snippets = CucumberOptions.SnippetType.UNDERSCORE,
    glue = {"com.flowable.testing.glue"}
)
public class CucumberFeatureRunner {

}
