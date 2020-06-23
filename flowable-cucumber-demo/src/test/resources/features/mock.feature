@Process
Feature: The greet process greets the initiator using service tasks
  Background:
    Given a Process Engine is running
    Given the process 'processes/greet_process_expression.bpmn20.xml' is deployed as 'greet_process'

  # Example of a process where a service task is mocked
  Scenario: The service "greetService" is mocked
    Given the bean 'greetTaskDelegateMock' is mocked with the following class: 'org.flowable.testing.demo.GreetTaskDelegateMock'
    When the process 'greet_process' is started by 'admin'
    Then the process is completed
    And the process variable 'message' is 'Hello admin'
