@pending
Feature: Delete COs and all associated AOs

As a Departmental Administrator, I want to delete a Course Offering and all of its associated Activity Offerings so that
it will no longer be offered for the term.

  Background:
    Given I am logged in as a Schedule Coordinator

  @pending
  Scenario: Cancel deletion and deletion of CO-COC view with single CO
    When I create a Course Offering with Draft Activity Offerings
    And I cancel the deletion of a Course Offering in Course Offering Code view
    And the Course Offering is not deleted
    And I delete this Course Offering
    Then the deleted course offering does not appear on the list of available Course Offerings

  @pending
  Scenario: Cancel deletion and deletion of CO-COC view with multiple COs
    When I create multiple Course Offerings with Draft Activity Offerings
    And I cancel the deletion of the Course Offerings in Course Offering Code view
    And the Course Offerings are not deleted
    And I delete these Course Offerings
    Then the deleted course offerings do not appear on the list of available Course Offerings

  @pending
  Scenario: Cancel deletion and deletion of CO - Subject Code view
    When I create a Course Offering with Draft Activity Offerings
    And I cancel the deletion of a Course Offering in Subject Code view
    And the Course Offering is not deleted
    And I delete this Course Offering in Subject Code view
    Then the deleted course offering does not appear on the list of available Course Offerings