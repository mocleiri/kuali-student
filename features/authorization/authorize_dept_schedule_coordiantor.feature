@nightly
Feature: Department Schedule Coordinator Authorization

  Background:
    Given I am logged in as a Department Schedule Coordinator


  Scenario: Department Schedule Coordinator has read only access to seat pools
    When I navigate to the edit activity offering
    Then I do not have access to add or edit seat pools

  @pending
  Scenario: Department Schedule Coordinator has access to revise logistics in their department
    When I navigate to the edit activity offering logistics schedule page for a course in my department
    Then I have access to view the schedule

  @pending
  Scenario: Department Schedule Coordinator does not have edit access to courses not in their department
    When I navigate to manage course offerings for a course not in my department
    Then I do not have access to edit the course offering

