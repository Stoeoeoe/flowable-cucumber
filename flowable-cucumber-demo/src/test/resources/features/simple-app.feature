@App
Feature: App works as expected
  Background:
    Given a Process Engine is running
    Given an App engine is running
    Given a user with the id 'admin' exists


  Scenario: Case and process can be started
    Given the app 'apps/simple_app.bar' is deployed
    Then the process 'app_process' can be started by 'admin'
    Then the process 'hello_process' cannot be started by 'admin'