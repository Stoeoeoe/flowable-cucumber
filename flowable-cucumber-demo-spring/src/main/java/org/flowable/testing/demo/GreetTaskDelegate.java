package org.flowable.testing.demo;

import java.util.logging.Logger;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("greetTaskDelegate")
public class GreetTaskDelegate implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger(GreetTaskDelegate.class.getName());
    @Override
    public void execute(DelegateExecution execution) {
        String initiator = execution.getVariable("initiator", String.class);
        String message = "Hello " + initiator + "!";

        LOGGER.info("Will store message '" + message + "' in variable 'message'");
        execution.setVariable("message", message);

    }
}
