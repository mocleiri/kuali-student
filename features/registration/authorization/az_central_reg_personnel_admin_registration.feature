@nightly @blue_team
Feature: REG.AZ Central Registration Personnel using Admin Registration
  CR22.18 As a Central Registration Personnel I want to be the appropriate Role to have access to register students so
  that not any person that have access to the system can register students for courses

#KSENROLL-13721
  Scenario: CR22.18.1 Validate that a Central Registration Personnel has access to Admin Registration
    Given I am logged in as a Central Registration Personnel
    When I navigate to Admin Registration
    Then I have access to select a student

  Scenario: CR22.18.2 Validate that a Student does not have access to Admin Registration
    Given I am logged in as a Student
    When I navigate to Admin Registration
    Then I do not have access to the page
