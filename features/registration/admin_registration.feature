@nightly @blue_team
Feature: Admin Registration
  CR22.1 As a Central Registration Personnel I want to enter a student id so that basic student info is display and I can register the student

  CR22.2 As Central Registration Personnel I want to select a term so that I can display basic info of the term and register a student

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
