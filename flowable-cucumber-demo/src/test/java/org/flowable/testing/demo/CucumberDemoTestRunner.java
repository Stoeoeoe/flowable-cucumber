package org.flowable.testing.demo;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/",
        plugin = {"pretty"},
        snippets = CucumberOptions.SnippetType.UNDERSCORE,
        glue = {"org.flowable.testing.glue"}
)public class CucumberDemoTestRunner {
}
