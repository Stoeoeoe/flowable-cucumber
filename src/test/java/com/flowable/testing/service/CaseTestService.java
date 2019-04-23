package com.flowable.testing.service;

import org.flowable.cmmn.api.repository.CaseDefinition;
import org.flowable.cmmn.api.runtime.CaseInstance;

public class CaseTestService {

    private CaseInstance caseInstance;
    private CaseDefinition caseDefinition;

    public CaseTestService() {
    }

    public CaseInstance getCaseInstance() {
        return caseInstance;
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
