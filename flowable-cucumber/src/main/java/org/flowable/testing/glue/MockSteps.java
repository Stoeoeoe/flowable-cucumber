package org.flowable.testing.glue;

import org.flowable.engine.test.mock.Mocks;
import org.springframework.beans.BeansException;

import io.cucumber.java.en.Given;

public class MockSteps {

//    private ApplicationContext applicationContext;
/*
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

 */

    @Given("the mock with the key {string} is replaced with the Spring bean {string}")
    public void theServiceIsMockedByTheClass(String objectToBeMocked, String beanName) {
       // Object bean = applicationContext.getBean(beanName);
       // Mocks.register(objectToBeMocked, bean);
    }

}
