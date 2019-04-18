package com.flowable.testing;

import org.flowable.engine.ProcessEngine;

public class ProcessTestService {

    private ProcessEngine processEngine;

    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }
}
