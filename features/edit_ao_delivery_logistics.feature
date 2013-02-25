@draft
Feature: Edit an AO to allow changing of it's actual delivery logistics

  Background:
    Given I am logged in as a Schedule Coordinator

  Scenario: I am able to access the ActivityOfferingEdit-SchedulePage after the mass scheduling event in order to revise delivery logistics
    When I navigate to the edit activity offering logistics schedule page
    Then I have access to view the schedule

  #@pending
  #@bjg
  #Scenario: Revise an AO's actual delivery logistics
  #  When I revise an AO's actual delivery logistics
  #  Then the AO's delivery logistics shows the new schedule