package org.flowable.testing.bdd.example;

import java.util.logging.Logger;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("greetTaskMockDelegate")
public class GreetTaskMockDelegate implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger(GreetTaskDelegate.class.getName());
    @Override
    public void execute(DelegateExecution execution) {
        String initiator = execution.getVariable("initiator", String.class);
        String message = "MOCK: Hello " + initiator + "!";

        LOGGER.info("MOCK: Will store message '" + message + "' in variable 'message'");
        execution.setVariable("message", message);

    }
}

