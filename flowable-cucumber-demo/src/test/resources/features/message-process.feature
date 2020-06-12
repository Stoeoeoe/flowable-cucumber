@Process
Feature: Messages can be handled
  Background:
    Given a Process Engine is running
    Given the process 'processes/message_process.bpmn20.xml' is deployed as 'message_process'

  # Example of a process which uses a message
  Scenario: A process with a message waits for a message and then starts a new activity
    When the process 'message_process' is started by 'admin'
    And the user task 'task_1' is completed
    And the message 'test_message' is received
    Then the activity 'task_2' is active

    When the user task 'task_2' is completed
    Then the process is completed

  # Example of a process with a message having a payload
  Scenario: A process with a message having a payload
    When the process 'message_process' is started by 'admin'
    And the user task 'task_1' is completed
    And the message 'test_message' is received with the following payload:
      | my_field | hello world | string |

    Then the process variable 'my_field' is 'hello world'