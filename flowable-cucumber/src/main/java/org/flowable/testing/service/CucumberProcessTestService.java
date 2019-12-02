package org.flowable.testing.service;

import static org.flowable.testing.util.FlowableCucumberConstants.ENGINE_PROCESS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;

public class CucumberProcessTestService {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;


    private Map<String, Map<String, Object>> definitionMap = new HashMap<>();
    private List<ProcessInstance> processInstances = new ArrayList<>();

    private ProcessInstance processInstance;

    public CucumberProcessTestService(RepositoryService repositoryService, RuntimeService runtimeService) {
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public String getProcessInstanceId() {
        return processInstance.getId();
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public <T> T getDefinition(String type, String key) {
        T definition = null;
        if(definitionMap.containsKey(type) && definitionMap.get(type).containsKey(key)) {
            definition = (T)definitionMap.get(type).get(key);
        } else {
            definition = getDefinitionFromRepository(type, key);
        }

        if (definition != null) {
            return definition;
        } else {
            throw new RuntimeException("Definition " + key + " of type '" + type + "' does not exist" );
        }
    }
    private <T> T getDefinitionFromRepository(String type, String key) {
        T definition = null;
        if(ENGINE_PROCESS.equals(type)) {
            definition = (T)repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
        }
        return definition;
    }

    // TODO: Is this needed? Probably can use the process instance directly
    public String getProcessExecutionId() {
        return runtimeService.createExecutionQuery().processInstanceId(getProcessInstanceId()).onlyProcessInstanceExecutions().singleResult().getId();
    }

    public List<String> getAllExecutionIds() {
        return runtimeService.createExecutionQuery()
            .processInstanceId(getProcessInstanceId())
            .list()
            .stream()
            .map(Execution::getId)
            .collect(Collectors.toList());
    }

    public <T> void addDefinition(String type, String key, Object definition) {
        if(!definitionMap.containsKey(type)) {
            definitionMap.put(type, new HashMap<>());
        }
        Map<String, Object> typeMap = definitionMap.get(type);
        typeMap.put(key, definition);
    }

    public boolean hasDefinition(String type, String key) {
        if(!definitionMap.containsKey(type)) {
            definitionMap.put(type, new HashMap<>());
        }
        Map<String, Object> typeMap = definitionMap.get(type);
        return typeMap.containsKey(key);
    }

    /*
    public ProcessDefinition getDeployedProcessDefinition(String processDefinitionKey) {
        ProcessDefinition processDefinition;
        if (processDefinitionMap.containsKey(processDefinitionKey)) {
            processDefinition = processDefinitionMap.get(processDefinitionKey);
        } else {
            processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).singleResult();
            if(processDefinition == null) {
                throw new RuntimeException("Could not find a deployed process with the key '" + processDefinitionKey + "'");
            }
        }
        return processDefinitionMap.get(processDefinitionKey);
    }

     */
}
