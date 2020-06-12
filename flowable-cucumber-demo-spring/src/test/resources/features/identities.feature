@Process
Feature: The identity links of a process work. It assumes that only assignees are supposed to complete tasks.
  Background:
    Given the following users exist:
     | admin        |
     | accountant   |
     | lineManager  |
    And the following groups exist:
      | adminUsers   |
      | accounting   |
      | lineManagers |
    And the user 'admin' is a member of the following groups:
      | adminUsers    |
      | accounting    |
      | lineManagers  |
    And the user 'accountant' is a member of the group 'accounting'
    And the user 'lineManager' is a member of the group 'lineManagers'
    And the process 'processes/identity_process.bpmn20.xml' is deployed as 'identity_process'

  Scenario: The user admin can complete all tasks
    When the process 'identity_process' is started by 'admin'
    Then the user 'admin' is 'candidate' of the user task 'accountingTask'

    When the user task 'accountingTask' is claimed by 'admin'
    And the user task 'accountingTask' is completed
    Then the user 'admin' is 'candidate' of the user task 'lineManagerTask'

    When the user task 'lineManagerTask' is claimed by 'admin'
    And the user task 'lineManagerTask' is completed
    Then the process is completed

  Scenario: The accountant can complete the first task, the lineManager cannot
    When the process 'identity_process' is started by 'accountant'
    Then the user 'accountant' is 'candidate' of the user task 'accountingTask'
    And the user 'lineManager' is not 'candidate' of the user task 'accountingTask'
