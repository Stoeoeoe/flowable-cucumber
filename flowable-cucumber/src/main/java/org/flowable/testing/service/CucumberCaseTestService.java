package org.flowable.testing.service;

import org.flowable.cmmn.api.repository.CaseDefinition;
import org.flowable.cmmn.api.runtime.CaseInstance;

public class CucumberCaseTestService {

    private CaseInstance caseInstance;
    private CaseDefinition caseDefinition;

    public CucumberCaseTestService() {
    }

    public CaseInstance getCaseInstance() {
        return caseInstance;
    }

    public String getCaseInstanceId() {
        return caseInstance.getId();
    }

    public void setCaseInstance(CaseInstance caseInstance) {
        this.caseInstance = caseInstance;
    }

    public CaseDefinition getCaseDefinition() {
        return caseDefinition;
    }

    public void setCaseDefinition(CaseDefinition caseDefinition) {
        this.caseDefinition = caseDefinition;
    }
}
