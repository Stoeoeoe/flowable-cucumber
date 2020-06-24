package org.flowable.testing.glue;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.When;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.test.mock.Mocks;
import org.flowable.testing.service.CucumberProcessTestService;
import org.flowable.testing.service.FlowableServices;
import org.flowable.testing.util.CucumberVariableUtils;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;

import io.cucumber.java.en.Given;

import java.util.Map;

public class MockSteps {

    private final RuntimeService runtimeService;
    private final CmmnRuntimeService cmmnRuntimeService;
    private final CucumberProcessTestService cucumberProcessTestService;

    //    private ApplicationContext applicationContext;
/*
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

 */
    public MockSteps(FlowableServices flowableServices, CucumberProcessTestService cucumberProcessTestService) {
        this.runtimeService = flowableServices.getRuntimeService();
        this.cmmnRuntimeService = flowableServices.getCmmnRuntimeService();
        this.cucumberProcessTestService = cucumberProcessTestService;
    }

    @Given("the bean {string} is mocked with the following class: {string}")
    public void theServiceIsMockedByTheClass(String objectToBeMocked, String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find class to mock: " + className);
        }
        Object mock = Mockito.mock(clazz);

        //  Object bean = applicationContext.getBean(beanName);
        Mocks.register(objectToBeMocked, mock);
    }

    @Given("the service task with the key {string} is ignored")
    public void theServiceTaskWithTheKeyIsIgnored(String activityId) {

    }

    @Given("the service task with the key {string} is ignored and returns the following value:")
    public void theServiceTaskWithTheKeyIsIgnored(String activityId, DataTable variables) {
        Map<String, Object> variableMap = CucumberVariableUtils.getMapFromDataTable(variables);
        String processInstanceId = cucumberProcessTestService.getProcessInstanceId();
    }

    @After
    public void resetMocks() {
        Mocks.reset();
    }

}
