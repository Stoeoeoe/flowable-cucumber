package org.flowable.testing.demo;


import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class GreetTaskDelegateMock implements JavaDelegate {

    Logger logger = Logger.getLogger(GreetTaskDelegateMock.class.getName());

    @Override
    public void execute(DelegateExecution execution) {
        String message = "Hello " + execution.getVariable("initiator");
        logger.fine("This is a mock for the Greet Service. Message: " +  message);
        execution.setVariable("message", message);
    }
}
