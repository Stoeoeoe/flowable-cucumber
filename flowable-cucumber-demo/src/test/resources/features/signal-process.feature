@Process
# Use the async hook to execute jobs every time after every stop to trigger the signal
@Async
Feature: Signals can be handled
  Background:
    Given a Process Engine is running
    Given the process 'processes/signal_process.bpmn20.xml' is deployed as 'signal_process'
    Given the process 'processes/global_signal_process.bpmn20.xml' is deployed as 'global_signal_process'

  # Example of a process having a event sub process which throws a local signal
  Scenario: A process is started and a signal is thrown which will terminate the process and set the "message" variable to "signal triggered"
    When the process 'signal_process' is started by 'admin'
    And the user task 'userTask1' is completed
    And the local signal 'test_signal' is thrown
    Then the process is completed
    And the process variable 'message' is 'signal thrown!'

  # Same process but without the signal
  Scenario: A process is started and no signal is thrown, there is no variable with the name "message"
    When the process 'signal_process' is started by 'admin'
    And the user task 'userTask1' is completed
    Then the process is not yet completed
    And the process variable 'message' does not exist

  # Start a process by a signal
  Scenario: A process is started when a global signal is thrown.
    When the global signal 'my_global_signal' is thrown
    Then there is 1 process with the definition key 'global_signal_process'
    But there are 0 processes with the definition key 'signal_process'
