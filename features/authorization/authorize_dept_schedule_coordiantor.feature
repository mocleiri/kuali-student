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

  @wip
  Scenario: Department Schedule Coordinator Carol can access the Manage CO set of pages for COs for her own admin org
    Given I am in working on a term in "Open" SOC state
    When I manage course offerings for a subject code in my department
    Then I can view course offering details
    And I can add new course offerings
    And I can approve course offerings for scheduling
    And I can delete course offerings
    And I can edit course offerings
    And I can copy course offerings

  @wip
  Scenario: Department Schedule Coordinator Carol can access the Manage AO set of pages for COs for her own admin org
    Given I am in working on a term in "Open" SOC state
    When I manage a course offering in my department
    Then I can view the activity offering details
    And the next, previous and list all course offering links are enabled
    And the add new activity button is enabled
    And the delete activity offering operation is enabled
    And the edit activity offering operation is enabled
    And the copy activity offering operation is enabled

  Scenario: Department Schedule Coordinator Carol does not have access to create CO's not in her admin org
    When I navigate to create course offerings and designate a valid term and Catalog Course Code not in my department
    Then I do not have access to create the course offering

  Scenario: Department Schedule Coordinator Carol has access to create CO's in her admin org
    When I navigate to create course offerings and designate a valid term and Catalog Course Code in my department
    Then I have access to create the course offering from catalog
    And I have access to create the course from an existing offering
