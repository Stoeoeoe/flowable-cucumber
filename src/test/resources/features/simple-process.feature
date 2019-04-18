
# @Process
Feature: The outcome of the simple decision is correct
  Background:
    Given an in-memory process engine is running

  Scenario: The country 'ch' was selected in the first form
    Given the process 'simple_process' is deployed
    Then the process is started
    And the user task 'task1' was filled as follows:
    | Variable      | Value             |
    | country       | ch                |