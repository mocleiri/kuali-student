@nightly @red_team

Feature: REG.Filter by Facets
  CR 19.10 - As a student I want to be able to apply a variety of search criteria
             so that I can refine and expand my search results (Large format)

  Background:
    Given I am logged in as a Student

  #KSENROLL-13741
  Scenario: Verify course search results using seats available facet
    When I search for History courses in the Course Search Page
    And I narrow the search results to courses with available seats
    Then I should see only courses with available seats

  #KSENROLL-13741
  Scenario: Verify search results using course level facet
    When I search for courses in the Course Search Page
    And I narrow the search results by a specific course level
    Then I should see only courses with the specific course level

  #KSENROLL-13741
  Scenario: Verify search results using Course prefix facet
    When I search for courses with multiple prefixes in the Course Search Page
    And I narrow the search results by a specific course prefix
    Then I should see only courses with the specific course prefix

#KSENROLL-13741
  Scenario: Verify search results using multiple facets
    When I search for courses with multiple prefixes in the Course Search Page
    And I narrow the search results by a specific course level and a specific course prefix
    Then I should see only courses with the specific course level and the specific course prefix

  #KSENROLL-13741
  Scenario: Verify that as a student I am able to undo any filtering performed using any facet
    When I narrow the search results using any facet
    And I undo the filtering performed using the specified facet
    Then I should see the courses in the search results without any filtering being applied
