@blue_team
Feature: Admin Registration
  CR22.1 As a Central Registration Personnel I want to enter a student id so that basic student info is display and I can register the student

  Background:
    Given I am logged in as admin

#KSENROLL-13422
  Scenario: CR22.1.1 Verify that valid student is entered
    When I attempt to load a student by valid student Id
    Then student information and change term section is displayed"


  Scenario: CR22.1.2 Verify error message when entering invalid student
    When I attempt to load a student by invalid student Id
    Then a validation error message displayed stating "Invalid student Id."