@nightly
Feature: BT.Course Section

Background:
Given I am logged in as a Student

  Scenario: Verify that I am able to see the section details of the course in the Course Details page
    When I search for a certain course on course search page
    And I navigate to the Course Section Details page
    Then I should be able to view the section details about the course


  Scenario:Verify that I am able to see the format offerings of a course offering and its associated activity offering so that I can decide what to add to my plan
    When I search for course which has format offerings on the course search page
    And I navigate to the Course Section Details page
    Then I should be able to view the format offerings for the course




