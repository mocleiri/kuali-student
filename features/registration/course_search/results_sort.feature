@nightly @red_team

Feature: REG.Results Sort
  CR 19.12 - As a student I want to sort my search results so that I can view them more easily (large format)

  Background:
    Given I am logged in as a Student

#KSENROLL-13742 (this whole feature)
  Scenario: CS 19.12.1 Sorting by course code in both ascending and descending order.
    When I search for ENGL2 courses on the course search page
    And I sort the results by course code
    Then the course codes should be sorted in ascending order
    When I sort the results by course code
    Then the course codes should be sorted in descending order

  Scenario: CS 19.12.2 Sorting by title in both ascending and descending order.
    When I search for ENGL2 courses on the course search page
    And I sort the results by title
    Then the titles should be sorted in ascending order
    When I sort the results by title
    Then the titles should be sorted in descending order

  Scenario: CS 19.12.3 Sorting by credits in both ascending and descending order.
    When I search for CHEM courses on the course search page
    And I sort the results by credits
    Then the credits should be sorted in ascending order
    When I sort the results by credits
    Then the credits should be sorted in descending order

