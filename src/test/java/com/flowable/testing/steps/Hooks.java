package com.flowable.testing.steps;

import com.flowable.testing.config.CucumberTestConfiguration;
import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.engine.test.AppDeployment;
import org.flowable.cmmn.api.CmmnRepositoryService;
import org.flowable.cmmn.api.repository.CmmnDeployment;
import org.flowable.dmn.api.DmnDeployment;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.junit.After;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CucumberTestConfiguration.class})
public class Hooks {

    private final RepositoryService repositoryService;
    private final CmmnRepositoryService cmmnRepositoryService;
    private final AppRepositoryService appRepositoryService;
    private final DmnRepositoryService dmnRepositoryService;

    public Hooks(RepositoryService repositoryService, CmmnRepositoryService cmmnRepositoryService, AppRepositoryService appRepositoryService, DmnRepositoryService dmnRepositoryService) {
        this.repositoryService = repositoryService;
        this.cmmnRepositoryService = cmmnRepositoryService;
        this.appRepositoryService = appRepositoryService;
        this.dmnRepositoryService = dmnRepositoryService;
    }

    @After
    public void deleteAllDeployments() {
        repositoryService.createDeploymentQuery().list().stream().map(Deployment::getId).forEach(repositoryService::deleteDeployment);
/*
        cmmnRepositoryService.createDeploymentQuery().list().stream().map(CmmnDeployment::getId).forEach(cmmnRepositoryService::deleteDeployment);
        appRepositoryService.createDeploymentQuery().list().stream().map(AppDeployment::getId).forEach(d -> appRepositoryService::deleteDeployment);
        dmnRepositoryService.createDeploymentQuery().list().stream().map(DmnDeployment::getId).forEach(d -> dmnRepositoryService::deleteDeployment);
        */
    }
}
