@data_setup
Feature: Setup Environment

  Setup required data for test run

  Background:
    I am logged in as a Schedule Coordinator

  Scenario: Successfully rollover courses to put target term in open status
    When I initiate a rollover to allow create an open term
    Then the results of the rollover are available
    And the rollover can be released to departments
