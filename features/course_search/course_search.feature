@pending
Feature: Black.Course Search

  Background:
    Given I am logged in as a Student
    And I am on the KSAP Course Search Page

  Scenario:  Search for a course
    When I search for a course "ENGL101"
    Then the course "ENGL101" should appear in search results
    And I clear the search entry
    And the search entry should be cleared
    And the search results list should be cleared successfully

  Scenario:  Clear search entry
    When I enter the course in the search field
    And I clear the search entry
    Then the search entry should be cleared


#CCO 2.12A
