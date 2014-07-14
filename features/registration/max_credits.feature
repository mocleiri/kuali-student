@nightly @red_team

Feature: REG.Max Credits

  CR 10 - As an Administrator, I want the system to validate that a student's registered Activity Offerings
          do not exceed the maximum number of credits set by the institution so the student does not violate
          policy and have their registration changed.

    CR 10.1 - As an admin, I want to prevent students from registering for more credits
              than the institutional credit limit so they do not violate registration policy
    CR 10.2 - As a student, I want to be informed of any registration transactions that are not successful
              due to the enforcement of the credit limit so that I can change my registration accordingly
    CR 10.5 - As an administrator, I want the ability to have different max credits for different
              types of terms so I can manage enrollment for the term
    CR 10.7 - As a student, I want to resubmit a failed registration transaction so I don't have to reenter
              the information

  Background:
    Given I am using a mobile screen size
    Given I log in to student registration as student2

#KSENROLL-13019
  Scenario: CR 10.1/CR 10.2 - Attempt to register for courses that exceed the spring term credit limit
    When I add courses to my registration cart that would exceed the spring term credit limit
    And I attempt to register for the courses
    Then there is a message indicating that registration failed
    And there is a message indicating that I have registered for a credit amount over the spring term credit limit
    And I cannot register for another course

  #KSENROLL-13024
  Scenario: CR 10.5 - Attempt to register for courses that exceed the summer term credit limit
    When I add courses to my registration cart that would exceed the summer term credit limit
    And I attempt to register for the courses
    Then there is a message indicating that I have registered for a credit amount over the summer term credit limit

  #KSENROLL-13027
  Scenario: CR 10.7 - Re-submit a course I was previously unable to register for because of credit limits
    When I log in to student registration as student3
    And I attempt to register for courses that would exceed the summer term credit limit
    Then there is a message indicating that I have registered for a credit amount over the summer term credit limit
    When I elect to keep the failed course in my cart
    And I remove a course from my schedule
    Then I am able to successfully register for the failed course
   # Suspicion that next feature file is failing on login because we are still logged in here, so logout
    * I log out from student registration
