package org.flowable.testing.glue;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.api.repository.AppDeployment;
import org.flowable.testing.service.FlowableServices;
import org.springframework.core.io.ClassPathResource;


import io.cucumber.java.en.Given;
import org.flowable.testing.service.CucumberProcessTestService;

/**
 * Cucumber Glue responsible for all steps regarding app deployments.
 */
public class AppSteps {

    Logger logger = Logger.getLogger(AppSteps.class.getName());

    private final AppRepositoryService appRepositoryService;
    private final CucumberProcessTestService cucumberProcessTestService;

    public AppSteps(FlowableServices flowableServices, CucumberProcessTestService cucumberProcessTestService) {
        this.appRepositoryService = flowableServices.getAppRepositoryService();
        this.cucumberProcessTestService = cucumberProcessTestService;
    }

    /**
     * Deploys an app under the specified resource.
     * @param appResource The resource (e.g. in the classpath) where a zip/bar app file is located.
     */
    @Given("the app {string} is deployed")
    public void theAppIsDeployed(String appResource) {
        // TODO: Replace with non-spring implementation
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
