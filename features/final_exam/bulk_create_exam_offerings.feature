@nightly @blue_team
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
