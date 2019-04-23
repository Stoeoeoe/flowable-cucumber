
@Process
Feature: The simple process is routed correctly
  Background:
    Given an in-memory process engine is running
    Given the process 'processes/simple_process.bpmn20.xml' is deployed
    Given the form 'forms/firstForm.form' is deployed

  Scenario: The country 'es' was selected in the first form
    When the process is started by 'admin'
    And the user task 'firstUserTask' is completed with the following form data:
      | country         | es                | string      |
      | age             | 1.0               | double      |
    Then the process is completed
    And the process variables are as follows:
      | country         | es                | string      |


  Scenario: The country 'ch' was selected in the first form
    When the process is started by 'admin'
    And the user task 'firstUserTask' is completed with the outcome 'accept' and the following form data:
      | Name            | Value             | Type        |
      | country         | ch                | string      |
    Then the process is not yet completed
    And the user task 'secondHumanTask' is completed
    Then the process is completed

