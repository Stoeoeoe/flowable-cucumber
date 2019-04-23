@Case
Feature: App works as expected
  Background:
    Given an in-memory CMMN engine is running
    Given an in-memory process engine is running
    Given an in-memory app engine is running


  Scenario: Case and process can be started
    Given the app 'apps/simple_app.bar' is deployed
    Then the case 'simple_case' can be started by 'admin'
    Then the process 'simple_process' can be started by 'admin'