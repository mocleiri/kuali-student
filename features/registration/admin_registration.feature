@blue_team
Feature: Admin Registration
  CR22.1 As a Central Registration Personnel I want to enter a student id so that basic student info is display and I can register the student

  CR22.2 As Central Registration Personnel I want to select a term so that I can display basic info of the term and register a student

  CR22.3 As Central Registration Personnel I want to view the Registered Courses for the Student and Term so that I can see if any additional actions are required for the registration of the student

  CR22.4 As Central Registration Personnel I want to view the Waitlisted Courses for the student and term so that I can see if any additional actions are required for the registration of the student

  Background:
    Given I am logged in as admin

#KSENROLL-13422
  Scenario: CR22.1.1 Verify that valid student is entered
    When I attempt to load a student by valid student Id
    Then student information and change term section is displayed

  Scenario: CR22.1.2 Verify error message when entering invalid student
    When I attempt to load a student by invalid student Id
    Then a validation error message displayed stating "Invalid student Id."

#KSENROLL-13424
  @pending
  Scenario: CR22.2.1 Verify that valid term is entered
    When I search for a term by valid term code
    Then term description is displayed stating "Fall 2012"

  @pending
  Scenario: CR22.2.2 Verify error message when entering invalid term
    When I search for a term by invalid term code
    Then error message is displayed stating "Change Term: Invalid term ID."

  @pending
  Scenario: CR22.2.3 Verify error message when no term is entered
    When I search for a term without entering a term code
    Then a required error message is displayed stating "Change Term: Term Code is required."

#KSENROLL-13425
  Scenario: CR22.3.1 Verify the registered courses table is populated and the correct credit count is shown
    When I search for a term by valid term code
    Then registered courses table is populated with courses
    And table header should contain total number of credits for registered courses

  Scenario: CR22.3.2 Verify the registered courses table is not populated for student with no registered courses
    When I search for a term by valid term code for student with no registered courses
    Then registered courses table is not populated with courses

  Scenario: CR22.3.3 Verify the registered course table is populated and default sort order is by course code
    When I search for a term by valid term code
    Then the default sort order for registered courses should be on course code

#KSENROLL-13426
  @pending
  Scenario: CR22.4.1 Verify the waitlisted courses table is populated and the correct credit count is shown
    When I search for a term by valid term code for student with waitlisted courses
    Then waitlisted courses table is populated with courses
    And table header should contain total number of credits for waitlisted courses

  @pending
  Scenario: CR22.4.2 Verify the waitlisted courses table is not populated for student with no waitlisted courses
    When I search for a term by valid term code
    Then waitlisted courses table is not populated with courses

  @pending
  Scenario: CR22.4.3 Verify the waitlisted course table is populated and default sort order is by course code
    When I search for a term by valid term code for student with waitlisted courses
    Then the default sort order for waitlisted courses should be on course code