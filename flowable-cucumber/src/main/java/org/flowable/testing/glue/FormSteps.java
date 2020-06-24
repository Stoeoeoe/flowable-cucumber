package org.flowable.testing.glue;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

import io.cucumber.java.After;
import org.flowable.form.api.FormDeployment;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.api.FormService;
import org.flowable.testing.service.FlowableServices;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;

public class FormSteps {

    private FormRepositoryService formRepositoryService;
    private FormService formService;

    private final Collection<String> createdDeployments = new HashSet<>();


    public FormSteps(FlowableServices flowableServices) {
        this.formService = flowableServices.getFormService();
        this.formRepositoryService = flowableServices.getFormRepositoryService();
    }


    @Given("the form {string} is deployed")
    public void theFormIsDeployed(String formResource) {
        FormDeployment deployment = this.formRepositoryService.createDeployment()
                .addClasspathResource(formResource)
                .deploy();

        createdDeployments.add(deployment.getId());
    }

    @After
    public void deleteDeployments() {
        for (String deployment : createdDeployments) {
            formRepositoryService.deleteDeployment(deployment);
        }

    }
}
