@nightly
Feature: BT.Course Search

  Background:
    Given I am logged in as a Student

  Scenario: CS2.1.1 Successfully search for a course and clear search results
    When I search for a course
    Then the course should appear in the search results
    And I clear the search entry
    And the search entry should be cleared successfully
    And the search results list should be cleared successfully

  Scenario: CS2.1.2 Clear search entry before searching
    When I enter the course in the search field
    And I clear the search entry
    Then the search entry should be cleared successfully

  @pending
  #KSAP-241, KSAP-321
    Scenario Outline: Successfully list any course with search text
    When I search for a course with "<text>" text option
    Then courses containing  "<verify>" text option appears
    Examples:
      | text   | verify |
      | ENGL   | ENGL   |
      | ENGL1XX| ENGL1|
      | ENGLISH| ENGL|


