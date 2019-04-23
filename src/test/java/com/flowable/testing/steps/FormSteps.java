package com.flowable.testing.steps;

import com.flowable.testing.config.CucumberTestConfiguration;
import cucumber.api.java.en.Given;
import org.flowable.form.api.FormDeployment;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.api.FormService;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = {CucumberTestConfiguration.class})
public class FormSteps {

    private final FormRepositoryService formRepositoryService;
    private final FormService formService;

    public FormSteps(FormRepositoryService formRepositoryService, FormService formService) {
        this.formRepositoryService = formRepositoryService;
        this.formService = formService;
    }


    @Given("the form '(.*)' is deployed")
    public void theFormIsDeployed(String formResource) {
        FormDeployment deployment = this.formRepositoryService.createDeployment()
                .addClasspathResource(formResource)
                .deploy();
        System.out.println("Deployed form " + formResource);
    }
}
