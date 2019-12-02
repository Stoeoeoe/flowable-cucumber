package org.flowable.testing.glue;

import java.util.logging.Logger;

import org.flowable.form.api.FormDeployment;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.api.FormService;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;
import io.cucumber.java8.En;

public class FormSteps  implements En {

    private Logger logger = Logger.getLogger(FormSteps.class.getName());

    @Autowired
    private FormRepositoryService formRepositoryService;

    @Autowired
    private FormService formService;

    @Given("the form {string} is deployed")
    public void theFormIsDeployed(String formResource) {
        FormDeployment deployment = this.formRepositoryService.createDeployment()
                .addClasspathResource(formResource)
                .deploy();

        logger.info("Deployed form " + formResource);
    }
}
