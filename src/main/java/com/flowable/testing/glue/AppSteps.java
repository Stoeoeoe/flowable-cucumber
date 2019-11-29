package com.flowable.testing.glue;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.api.repository.AppDeployment;
import org.springframework.core.io.ClassPathResource;

import com.flowable.testing.service.CucumberProcessTestService;

import io.cucumber.java.en.Given;
import io.cucumber.java8.En;

/**
 * Cucumber Glue responsible for all steps regarding app deployments.
 */
public class AppSteps implements En {

    Logger logger = Logger.getLogger(AppSteps.class.getName());

    private final AppRepositoryService appRepositoryService;
    private final CucumberProcessTestService cucumberProcessTestService;

    public AppSteps(AppRepositoryService appRepositoryService, CucumberProcessTestService cucumberProcessTestService) {
        this.appRepositoryService = appRepositoryService;
        this.cucumberProcessTestService = cucumberProcessTestService;
    }

    /**
     * Deploys an app under the specified resource.
     * @param appResource The resource (e.g. in the classpath) where a zip/bar app file is located.
     */
    @Given("the app {string} is deployed")
    public void theAppIsDeployed(String appResource) {
        ClassPathResource classPathResource = new ClassPathResource(appResource);
        try {
            ZipInputStream zis = new ZipInputStream(classPathResource.getInputStream());
            AppDeployment appDeployment = appRepositoryService.createDeployment()
                .addZipInputStream(zis)
                .deploy();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not load app '" + appResource + "'");
        }
    };

}
