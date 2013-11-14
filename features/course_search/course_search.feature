@pending
#feature file development in progress
Feature: Black.Course Search

  Background:
    Given I am logged in as a Student
# And I search for a course by course code ***new step definition required***

  Scenario:  Search for a course by course code
  #When I search for a course by course code ***new step definition required***
    Then the course should appear in the search results

  Scenario: Search for newly created academic calendar
    And I search for the calendar
    Then the calendar should appear in search results

  Scenario:  Search for a course
    When I search for a course
    Then the course should appear in the search results
    And I clear the search entry
    And the search entry should be cleared successfully

  Scenario:  Clear search result
    When I enter the course in the search field
    Then the course should appear in the search results
    And click clear button
    Then  entry should be cleared successfully
  #CCO 2.12A
