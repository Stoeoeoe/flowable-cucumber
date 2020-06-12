@Process
Feature: The timer process works as expected
  Background:
    Given a Process Engine is running
    Given the process 'processes/timer_process.bpmn20.xml' is deployed as 'timer_process'
    Given the current date is '2019-01-01' and the time is '10:00:00'

  Scenario: The user only waits 5 minutes, the second task is not available
    When the process 'timer_process' is started by 'admin'
    And the user task 'task1' is completed
    And 5 minutes passed
    And the activity 'task2' is not active

  Scenario: The user waits for 2 days, the second task is available
    When the process 'timer_process' is started by 'admin'
    And the user task 'task1' is completed
    And 2 days passed
    Then the activity 'task2' is active
