@Process
Feature: Async and triggering is demonstrated

  Background:
    Given a Process Engine is running
    Given the process 'processes/greet_process_async.bpmn20.xml' is deployed as 'greet_process_async'
    Given the process 'processes/trigger_process.bpmn20.xml' is deployed as 'trigger_process'

# Example of Async Tasks executed individually
  Scenario: The async task is executed properly (individual async execution)
    When the process 'greet_process_async' is started by 'admin'
    Then the process is not yet completed
    When the async service task 'greetUserAsyncTask' is executed
    Then the process is completed
    And the process variable 'message' is 'Hello admin!'

  #
  Scenario: The async task is executed properly (global async job execution)
    When the process 'greet_process_async' is started by 'admin'
    Then the process is not yet completed
    When all async jobs are executed
    Then the process is completed
    And the process variable 'message' is 'Hello admin!'

  #
  @Async
  Scenario: The async task is executed properly, all async actions are executed after each step due to the @Async tag
    When the process 'greet_process_async' is started by 'admin'
    Then the process is completed
    And the process variable 'message' is 'Hello admin!'

  Scenario: The process is halted until the async 'serviceTask' is triggered
    When the process 'trigger_process' is started by 'admin'
    Then the process is not yet completed
    When the activity 'serviceTask' is triggered asynchronously
    Then the activity 'serviceTask' is not active
    And the process is completed
