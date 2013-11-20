@pending
Feature: BT.Course Search

  Background:
    Given I am logged in as a Student

  Scenario: Successfully search for a course and clear search results
    When I search for a course
    Then the course should appear in the search results
    And I clear the search entry
    And the search entry should be cleared successfully
    And the search results list should be cleared successfully

  Scenario: Clear search entry before searching
    When I enter the course in the search field
    And I clear the search entry
    Then the search entry should be cleared successfully


#CCO 2.12A
