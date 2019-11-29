@App
Feature: App works as expected
  Background:
    Given an Process Engine is running
    Given an App engine is running
    Given a user with the id 'admin' exists


  Scenario: Case and process can be started
    Given the app 'apps/simple_app.bar' is deployed
    Then the process 'app_process' can be started by 'admin'