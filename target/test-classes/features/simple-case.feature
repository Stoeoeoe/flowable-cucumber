@Case
Feature: Simple case can be completed
  Background:
    Given an in-memory CMMN engine is running

  Scenario: All tasks are completed
    Given the case 'cases/simple_case.cmmn.xml' is deployed
    When the case is started by 'admin'
    Then the case is not yet completed
    When the human task 'task1' is completed
    When the human task 'task2' is completed
    Then the case is completed
