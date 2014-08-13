@nightly @blue_team
Feature: REG.Edit Course on Admin Registration
  CR23.2 As a Central Registration Personnel I want to be able to edit options for Registered courses for the student and term so that i can make changes to grading options; credit options and effective date

  Background:
    Given I am logged in as admin

#KSENROLL-13777
  Scenario: CR23.2.1 Verify default values are displayed for a course that is being edited
    When I attempt to edit a course with default values for Credit and Registration Options
    Then the default values are displayed on edit course dialog

  Scenario: CR23.2.2 Verify On Edit course, the inputs must be mandatory
    When I attempt to edit a registered course
    And I save the edited course with no effective date
    Then a message appears indicating that the effective date is required

  Scenario: CR23.2.3 Verify that Edited course is updated
    When I attempt to edit a registered course
    And I save the changes made to Registration Options and Effective Date
    Then a message appears indicating that the course has been updated successfully
