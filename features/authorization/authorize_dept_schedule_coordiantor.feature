@nightly
Feature: Department Schedule Coordinator

  Background:
    Given I am logged in as a Department Schedule Coordinator


  Scenario: Department Schedule Coordinator has read only access to seat pools
    When I navigate to the edit activity offering
    Then I do not have access to add or edit seat pools