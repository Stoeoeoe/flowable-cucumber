package com.flowable.testing.steps;

import com.flowable.testing.config.CucumberTestConfiguration;
import cucumber.api.java.en.Given;
import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.api.repository.AppDeployment;
import org.flowable.engine.RepositoryService;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CucumberTestConfiguration.class})
public class AppSteps {

    private final AppRepositoryService appRepositoryService;
    private final RepositoryService repositoryService;

    public AppSteps(AppRepositoryService appRepositoryService, RepositoryService repositoryService) {
        this.appRepositoryService = appRepositoryService;
        this.repositoryService = repositoryService;
    }

    @Given("^the app '(.*)' is deployed$")
    public void theAppIsDeployed(String appResources) {
        AppDeployment appDeployment = appRepositoryService.createDeployment()
                .addClasspathResource(appResources)
                .deploy();
        System.out.println();
    }
}
