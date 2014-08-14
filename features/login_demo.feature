@nightly
Feature: Login Demo

  Demonstrate Kuali Student Login

  Scenario: Successfully login and logout of application
    Given I am logged in as a Schedule Coordinator
    Then I am on the Kuali Student home page
    And I log out
