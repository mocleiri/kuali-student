@nightly
Feature: Department Schedule Coordinator Authorization

  Background:
    Given I am logged in as a Department Schedule Coordinator


  Scenario: Department Schedule Coordinator has read only access to seat pools
    When I navigate to the edit activity offering for a course offering in my department in "final edits"
    Then I do not have access to add or edit seat pools

  Scenario: Department Schedule Coordinator has access to revise logistics in their department
    When I navigate to the edit activity offering for a course offering in my department in "final edits"
    Then I have access to revise delivery logistics

  Scenario: Department Schedule Coordinator does not have edit access to courses not in their department
    When I navigate to manage course offerings for a course offering not in my department
    Then I do not have access to edit the course offering

  @pending
  Scenario: Department Schedule Coordinator Carol can access the Manage CO set of pages for COs for her own admin org
    When I navigate to manage course offerings for a course in my department
    Then I do not have access to edit the course offering

  @wip
  Scenario: Department Schedule Coordinator Carol does not have access to create CO's not in her admin org
    When I navigate to create course offerings and designate a valid term and Catalog Course Code not in my department
    Then I do not have access to create the course offering

  @pending
  Scenario: Department Schedule Coordinator Carol has access to create CO's in her admin org
    When I navigate to create course offerings and designate a valid term and Catalog Course Code in my department
    Then I have access to create the course offering from catalog
    And I have access to create the course from an existing offering