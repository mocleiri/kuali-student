@nightly @blue_team
Feature: REG.Drop Course on Admin Registration
  CR23.3 As a	Central Registration Personnel	I want to	the system to check Eligibility checks on drop of a course for the student	so that	i can ensure the student is stil eligibile for the registered courses

  CR23.4 As a Central Registration Personnel I want to be able to drop a course for the student and term, specifying the effective drop date so that i can make changes to the registered courses for the student

  Background:
    Given I am logged in as admin

  Scenario: CR23.3.1 Verify that an allow or deny warning message shows before a course is dropped from the registered courses table
    Given I have registered a student for a course that needed to be allowed in the term
    When I attempt to drop the registered course
    Then a message appears indicating that I need to allow or deny the drop of the course

#KSENROLL-13778
  Scenario: CR23.4.1 Verify that a student is no longer registered for a course after it has been dropped
    When I attempt to drop a registered course
    Then the student is no longer registered for the course
    And a message appears indicating that the course has been successfully dropped

  Scenario: CR23.4.2 Verify that a student's registration is moved from waitlisted to registered when a course is dropped for another student
    When I register multiple students for the same course
    Then the first student is registered for the course
    But the second student is waitlisted for the course
    When I drop the course for the first student
    Then that student is no longer registered for the course
    And the second student's course has moved from waitlisted to registered
