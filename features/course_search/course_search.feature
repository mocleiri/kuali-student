@pending
Feature: Black.Course Search

  Background:
    Given I am logged in as a Student
    And I am on the KSAP Course Search Page
# And I search for a course by course code ***new step definition required***

  Scenario:  Search for a course
    When I search for a course
    Then the course should appear in search results
    And I clear the search entry
    And the search entry should be cleared

  Scenario:  Clear search result
    When I enter the course in the search field
    And I clear the search entry
    Then the search entry should be cleared

#CCO 2.12A
