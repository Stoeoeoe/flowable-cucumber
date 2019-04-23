package com.flowable.testing.service;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.ConfigurationResource;
import org.flowable.engine.test.FlowableTest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ProcessTestService {

    private ProcessInstance processInstance;
    private ProcessDefinition processDefinition;


    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }
}
