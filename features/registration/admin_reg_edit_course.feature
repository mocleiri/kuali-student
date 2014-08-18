@nightly @blue_team
Feature: REG.Edit Course on Admin Registration
  CR23.1 As a Central Registration Personnel I want to the system to check Eligibility checks on edit of the student course registration so that i can ensure the student is stil eligibile for the registered courses

  CR23.2 As a Central Registration Personnel I want to be able to edit options for Registered courses for the student and term so that i can make changes to grading options; credit options and effective date

  Background:
    Given I am logged in as admin

#KSENROLL-13779
  Scenario: CR23.1.1 Verify the updated course displays after eligibility passed when editing a registered course
    When I edit a registered course by changing the Registration Options
    And I save the changes made to Registration Options
    Then the registered course is updated with the new Registration Options

  Scenario: CR23.1.2 Verify the updated course does not display after eligibility failed when editing a registered course
    When I edit a registered course by assigning more credits than the allowed maximum
    And I save the changed credits to be more than the allowed maximum
    Then the registered course is not updated with the new Course Credits
    And a message appears indicating that the updated course failed eligibility

  Scenario: CR23.1.3 Verify multiple course eligibility failed messages appear and the course is not displayed when saving the edited registered course
    When I register a student for a course with a time conflict
    And I add registered courses by selecting more credits than the allowed maximum
    And I save the changes made to course credits
    Then multiple failed eligibility messages appear
    And the registered course is not updated with the new Course Credits

  Scenario: CR23.1.4 Verify the updated course does not display after cancelling failed eligibility
    When I attempt to edit a registered course by adding more credits than the allowed maximum
    And I save the changes made to course credits
    And I deny the edited course to be updated
    Then the registered course is not updated with the new Course Credits

  Scenario: CR23.1.5 Verify the updated course is displayed after allowing failed eligibility
    When I attempt to edit a registered course by assigning more credits than the allowed maximum
    And I save the changes made to course credits
    And I allow the course for registration
    Then the registered course is updated with the new Registration Options

#KSENROLL-13777
  Scenario: CR23.2.1 Verify default values are displayed for a course that is being edited
    When I attempt to edit a course with default values for Credit and Registration Options
    Then the default values are displayed on edit course dialog

  @bug @KSENROLL-14124
  Scenario: CR23.2.2 Verify On Edit course, the inputs must be mandatory
    When I attempt to edit a registered course
    And I save the edited course with no effective date
    Then a message appears indicating that the effective date is required

  Scenario: CR23.2.3 Verify that Edited course is updated
    When I attempt to edit a registered course
    And I save the changes made to Registration Options and Effective Date
    Then a message appears indicating that the course has been updated successfully