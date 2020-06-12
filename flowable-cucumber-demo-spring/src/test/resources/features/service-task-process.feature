@Process
Feature: The greet process greets the initiator using service tasks
  Background:
    Given a Process Engine is running
    Given the process 'processes/greet_process.bpmn20.xml' is deployed as 'greet_process'

    # Example of Scenario Outline: Test several values at once
  Scenario Outline: The initiator is "admin"
    When the process 'greet_process' is started by '<user>'
    Then the process is completed
    And the process variables are as follows:
      | message | <message> | string |

    Scenarios:
      | user   | message       |
      | admin  | Hello admin!  |
      | kermit | Hello kermit! |

  # Example of a mock task
 # @Mock
 # Scenario: The service is mocked
 #   When the process 'greet_process' is started by 'admin'
 #   And the mock with the key 'greetTaskDelegate' is replaced with the Spring bean 'greetTaskMockDelegate'
 #   Then the process is completed
#    And the process variable 'message' is 'MOCK: Hello admin!'


