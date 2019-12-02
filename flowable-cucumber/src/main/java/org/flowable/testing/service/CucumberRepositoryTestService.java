package org.flowable.testing.service;

import java.util.HashMap;
import java.util.Map;

import org.flowable.app.api.repository.AppDefinition;
import org.flowable.cmmn.api.repository.CaseDefinition;
import org.flowable.dmn.model.DmnDefinition;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.form.api.FormDefinition;

public class CucumberRepositoryTestService {

    private final RepositoryService repositoryService;

    private Map<String, ProcessDefinition> processDefinitionMap = new HashMap<>();
    private Map<String, CaseDefinition> caseDefinitionMap = new HashMap<>();
    private Map<String, AppDefinition> appDefinitionMap = new HashMap<>();
    private Map<String, DmnDefinition> dmnDefinitionMap = new HashMap<>();
    private Map<String, FormDefinition> formDefinitionMap = new HashMap<>();

    public CucumberRepositoryTestService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

}
