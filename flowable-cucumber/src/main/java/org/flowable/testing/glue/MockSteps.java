package org.flowable.testing.glue;

import org.flowable.engine.test.mock.Mocks;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import io.cucumber.java.en.Given;
import io.cucumber.java8.En;

public class MockSteps implements En, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Given("the mock with the key {string} is replaced with the Spring bean {string}")
    public void theServiceIsMockedByTheClass(String objectToBeMocked, String beanName) {
        Object bean = applicationContext.getBean(beanName);
        Mocks.register(objectToBeMocked, bean);
    }

}
