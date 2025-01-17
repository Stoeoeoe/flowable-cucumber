package org.flowable.testing.demo;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class ConsoleLogger implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger(ConsoleLogger.class.getName());
    private Expression message;

    public Expression getMessage() {
        return message;
    }
    public void setMessage(Expression message) {
        this.message = message;
    }
    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info(message.getExpressionText());
    }
}
