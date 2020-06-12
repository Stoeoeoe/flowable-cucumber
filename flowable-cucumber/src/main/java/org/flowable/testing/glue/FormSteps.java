package org.flowable.testing.glue;

import java.util.logging.Logger;

import org.flowable.form.api.FormDeployment;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.api.FormService;
import org.flowable.testing.service.FlowableServices;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;

public class FormSteps {

    private FormRepositoryService formRepositoryService;
    private FormService formService;


    public FormSteps(FlowableServices flowableServices) {
        this.formService = flowableServices.getFormService();
        this.formRepositoryService = flowableServices.getFormRepositoryService();
    }


    @Given("the form {string} is deployed")
    public void theFormIsDeployed(String formResource) {
        FormDeployment deployment = this.formRepositoryService.createDeployment()
                .addClasspathResource(formResource)
                .deploy();
    }
}
