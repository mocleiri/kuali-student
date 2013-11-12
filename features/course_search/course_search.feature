@pending
#feature file development in progress
Feature: Black.Course Search

  Background:
    Given I am logged in as a Student
# And I search for a course by course code ***new step definition required***

  Scenario:  Search for a course by course code
  #When I search for a course by course code ***new step definition required***
    Then the course should appear in the search results


  Scenario:  Clear search field text
    When I enter the course in the search field
    And  click clear button
    Then entry should be cleared successfully

  Scenario:  Clear search result
    When I enter the course in the search field
    Then the course should appear in the search results
    And click clear button
    Then  entry should be cleared successfully
  #CCO 2.12A
