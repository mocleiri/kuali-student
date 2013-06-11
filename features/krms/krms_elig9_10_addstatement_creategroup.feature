Feature: KRMS ELIG9.10 Add Statement and Create Group

  Background:
    Given I am logged in as admin
    And I have navigated to the agenda page for "Corequisite" for term "201301" and course "HIST210"

  #ELIG9.10.EB1 (KSENROLL-7049)
  @pending
  Scenario: Setup one level of data using Add Statement button
    When I want to add a new statement to the selected agenda section
    And I add a new "text" statement with text "Free Text 1"
    And I add a "text" statement after node "A" with text "Free Text 2"
    And I add a "text" statement after node "C" with text "Free Text 3"
    Then the "edit" tab should have the text "B. Must meet all of the following"
    And the "edit" tab should have the text "A. Free Text 1"
    And the "edit" tab should have the text "C. Free Text 2"
    And the "edit" tab should have the text "D. Free Text 3"
    When I commit changes made to the proposition

  #ELIG9.10.EB2 (KSENROLL-7049)
  @pending
  Scenario: Setup second level of data (Groups) using Create Group button
    When I want to edit the selected agenda section
    And I create a group with node "B" by adding a "text" statement with text "Free Text 1b"
    And I create a group with node "D" by adding a "text" statement with text "Free Text 2b"
    And I create a group with node "F" by adding a "text" statement with text "Free Text 2c"
    When I commit and return to see the changes made to the proposition
    And I want to edit the selected agenda section
    Then the "edit" tab should have the text "A. Must meet all of the following"
    And the "edit" tab should have the text "B. Must meet all of the following"
    And the "edit" tab should have the text "D. Must meet all of the following"
    And the "edit" tab should have the text "H. Must meet all of the following"
    And the "edit" tab should have the text "E. Free Text 1b"
    And the "edit" tab should have the text "F. Free Text 2c"
    And the "edit" tab should have the text "J. Free Text 2b"
