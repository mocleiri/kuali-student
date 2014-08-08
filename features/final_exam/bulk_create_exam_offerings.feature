@nightly @yellow_team
Feature: CO.Bulk Create Exam Offerings

  FE 3.11: As a Central Administrator I want to create exam offerings in bulk at a particular stage in the course scheduling process
  so that exam offerings will be created when course scheduling has reached a stable state to manage exam offerings

  Background:
    Given I am logged in as admin

  Scenario: FE 3.11.1 Verify rollover into a term with no exam period is allowed but Create Exam is not possible until exam period is set
    Given I create an Academic Calendar and add an official term
    And I rollover the term to a new academic term that has no exam period
    But I cannot generate 'bulk' exam offerings for the new term
    When I add an Exam Period to the new term
    Then I can generate 'bulk' exam offerings for the new term

  Scenario: FE 3.11.2 Verify correct exam offering scheduling states for CO-driven exams when bulk create EO process has been run
    Given I create an Academic Calendar and add an official term
    And I add an Exam Period to the new term
    And I create multiple course offerings with CO-driven exams with course codes matching and not matching entries on the exam matrix
    When I generate 'bulk' exam offerings for the new term
    Then the scheduling state for a CO-driven EO RSI with a matching matrix entry is 'Unscheduled'
    And the scheduling state for a CO-driven EO RSI with no matching matrix entry is 'Matrix Error'
    And the scheduling state for a CO-driven EO RSI for a course offering configured to not use the matrix is 'Unscheduled'

  Scenario: FE 3.11.3 Verify correct exam offering scheduling states for AO-driven exams when bulk create EO process has been run
    Given I create an Academic Calendar and add an official term
    And I add an Exam Period to the new term
    And I create multiple course offerings with AO-driven exams with scheduling information matching and not matching entries on the exam matrix
    When I generate 'bulk' exam offerings for the new term
    Then the scheduling state AO-driven EO RSI with a matching matrix entry is 'Unscheduled'
    And the scheduling state AO-driven EO RSI with no matching matrix entry is 'Matrix Error'
    And the scheduling state AO-driven EO RSI with no AO scheduling information is 'Matrix Error'
    And the scheduling state AO-driven EO RSI for a course offering configured to not use the matrix is 'Unscheduled'