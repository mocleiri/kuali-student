@wip @yellow_team
Feature: CO.Manage Admin Comments

  CO 27.1 As an Administrator I want to add a comment to a course offering so that special information or requests are recorded

  CO 27.2 As an Administrator I want to add a comment on an Activity Offering so that special information or requests are recorded

  CO 27.3 As an Administrator I want to view comments placed on a Course Offering so that I can assess or act on the information

  CO 27.4 As an Administrator I want to to view comments placed on an Activity Offering so that I can assess or act on the information

  CO 27.5 As an Administrator I want to to delete a comment attached to a Course Offering so that I can remove information that is no longer relevant

  CO 27.6 As an Administrator I want to delete a comment attached to an Activity Offering so that I can remove information that is no longer relevant

  CO 27.7 As an Administrator I want to to edit a comment attached to a Course Offering so that I can correct or update information

  CO 27.8 As an Administrator I want to edit a comment attached to an Activity Offering so that I can correct or update information

  CO 27.9 As an Administrator I want to have appropriate authorization to add or edit comments

  CO 27.10 As an Administrator I want to be able to include rich text formatting in administrative comments on Course Offerings and Activity
  Offerings so that I can draw attention to text or include actionable urls and email addresses

  Scenario: CO 27.1.1 Verify successful add of admin comment to a course offering
    Given I am logged in as Central Admin
    And I choose the Comments link for a course offering
    When I add a new comment and select Add Comment
    Then the comment is saved and can be viewed immediately along with the created by userid and creation date

  Scenario: CO 27.2.1 Verify successful add of admin comment to an activity offering
    Given I am logged in as Central Admin
    And I choose the Comments link for an activity offering
    When I add a new comment and select Add Comment
    Then the comment is saved and can be viewed immediately along with the created by userid and creation date

  Scenario: CO27.3.1 Verify user can view an existing admin comment on a course offering
    Given I am logged in as Central Admin
    And I choose the Comments link for a course offering with existing comments
    When I open the Admin Comments window I can see the existing comments

  Scenario: CO27.4.1 Verify user can view an existing admin comment on an activity offering
    Given I am logged in as Central Admin
    And I choose the Comments link for an activity offering with existing comments
    When I open the Admin Comments window I can see the existing comments

  Scenario: CO27.5.1 Verify successful deletion of comment on course offering
    Given I am logged in as Central Admin
    And I choose the Comments link for a course offering with existing comments
    When I select the delete icon beside an existing comment
    Then the comment is grayed out and an undo link replaces the delete and edit icons
    And when I close the comment pop-up the comment is deleted

  Scenario: CO27.5.2 Verify user can undo deletion of comment on course offering
    Given I am logged in as Central Admin
    And I choose the Comments link for a course offering with existing comments
    When I select the delete icon beside an existing comment
    And then choose the undo link
    Then the comment is no longer grayed out
    And when I close the comment pop-up the comment is not deleted

  Scenario: CO27.6.1 Verify successful deletion of comment on activity offering
    Given I am logged in as Central Admin
    And I choose the Comments link for an activity offering with existing comments
    When I select the delete icon beside an existing comment
    Then the comment is grayed out and an undo link replaces the delete and edit icons
    And when I close the comment pop-up the comment is deleted

  Scenario: CO27.6.2 Verify user can undo deletion of comment on activity offering
    Given I am logged in as Central Admin
    And I choose the Comments link for an activity offering with existing comments
    When I select the delete icon beside an existing comment
    And then choose the undo link
    Then the comment is no longer grayed out
    And when I close the comment pop-up the comment is not deleted

  Scenario: CO27.7.1 Verify successful edit of comment on course offering
    Given I am logged in as Central Admin
    And I choose the Comments link for a course offering with existing comments
    When I select the edit icon beside an existing comment
    Then the comment becomes editable and a save button and cancel link appear beneath it
    And when I make changes to the comment and click save
    Then the changes to the comment are saved

  Scenario: CO27.7.2 Verify user can cancel the editing of comment on course offering
    Given I am logged in as Central Admin
    And I choose the Comments link for a course offering with existing comments
    When I select the edit icon beside an existing comment
    Then the comment becomes editable and a save button and cancel link appear beneath it
    And when I make changes to the comment and click cancel
    Then the changes to the comment are not saved and the original version is retained

  Scenario: CO27.8.1 Verify successful edit of comment on activity offering
    Given I am logged in as Central Admin
    And I choose the Comments link for an activity offering with existing comments
    When I select the edit icon beside an existing comment
    Then the comment becomes editable and a save button and cancel link appear beneath it
    And when I make changes to the comment and click save
    Then the changes to the comment are saved

  Scenario: CO27.8.2 Verify user can cancel the editing of comment on activity offering
    Given I am logged in as Central Admin
    And I choose the Comments link for an activity offering with existing comments
    When I select the edit icon beside an existing comment
    Then the comment becomes editable and a save button and cancel link appear beneath it
    And when I make changes to the comment and click cancel
    Then the changes to the comment are not saved and the original version is retained

  Scenario: CO 27.9.1 DSC (Carol) has read-only permission on course offering comments created by others in her org
    Given I am logged in as Carol (DSC)
    And there is an admin comment on a course offering in Carol's admin org created by another user
    When I select the Comments link for the course offering with existing comments
    Then I am not able to edit the comments entered by another user

  Scenario: CO 27.9.2 DSC (Carol) has read-only permission on course offering comments created for courses in another org
  GivenI am logged in as Carol (DSC)
    And there is an admin comment on a course offering in an admin org outside of Carol's
    When I select the Comments link for the course offering with existing comments
    Then I am not able to edit the comments

  Scenario: CO 27.9.3 DSC (Carol) has add permission on course offering comments created by herself on courses in her org
    Given I am logged in as Carol
    When I select the Comments link for a course offering within my org
    Then I am able to add new comments

  Scenario: CO 27.9.4 DSC (Carol) has edit permission on course offering comments created by herself on courses in her org
    Given I am logged in as Carol
    When I select the Comments link for a course offering within my org that has comments
    Then I am able to edit my own existing comments

  Scenario: CO27.9.5 DSC (Carol) has delete permission on course offering comments created by herself on courses in her org
    Given I am logged in as Carol
    When I select the Comments link for a course offering within my org
    Then I am able to delete my own existing comments

  Scenario: CO 27.9.6 DSC (Carol) has read-only permission on activity offering comments created by others in her org
    Given I am logged in as Carol (DSC)
    And there is an admin comment on an activity offering in Carol's admin org created by another user
    When I select the Comments link for the activity offering with existing comments
    Then I am not able to edit the comments entered by another user

  Scenario: CO 27.9.7 DSC (Carol) has read-only permission on activity offering comments created for activities in another org
  GivenI am logged in as Carol (DSC)
    And there is an admin comment on an activity offering in an admin org outside of Carol's
    When I select the Comments link for the activity offering with existing comments
    Then I am not able to edit the comments

  Scenario: CO 27.9.8 DSC (Carol) has add permission on activity offering comments created by herself on activities in her org
    Given I am logged in as Carol
    When I select the Comments link for an activity offering within my org
    Then I am able to add new comments

  Scenario: CO 27.9.9 DSC (Carol) has edit permission on activity offering comments created by herself on activities in her org
    Given I am logged in as Carol
    When I select the Comments link for an activity offering within my org with existing comments
    Then I am able to edit my existing comments

  Scenario: CO27.9.10 DSC (Carol) has delete permission on course offering comments created by herself on activities in her org
    Given I am logged in as Carol
    When I select the Comments link for an activity offering within my org
    Then I am able to delete my own existing comments

  Scenario: CO27.10.1 Verify user can include url and email hyperlinks in new admin comments on course and activity offerings
    Given I am logged in as Central Admin
    And I choose the Comments link for a course offering
    When I add a new comment with a url or email hyperlink and select Add Comment
    Then the comment is saved with a working hyperlink
    And can be viewed immediately along with the created by userid and creation date

  Scenario: CO27.10.2 Verify user can include url and email hyperlinks in existing admin comments on course and activity offerings
    Given I am logged in as Central Admin
    And I choose the Comments link for an activity offering with an existing comment
    When I add a url or email hyperlink and select Add Comment
    Then the comment is saved with a working hyperlink
    And can be viewed immediately along with the created by userid and creation date

  Scenario: CO27.10.3 Verify user can include typography elements such as underlining, bolding, and italics in new admin comments on course and activity offerings
    Given I am logged in as Central Admin
    And I choose the Comments link for a course offering
    When I add a new comment with underlining, bolding and italics and select Add Comment
    Then the comment is saved with the typography elements displayed
    And can be viewed immediately along with the created by userid and creation date

  Scenario: CO27.10.4 Verify user can include typography elements such as underlining, bolding, and italics in existing admin comments on course and activity offerings
    Given I am logged in as Central Admin
    And I choose the Comments link for an activity offering with an existing comment
    When I add underlining, bolding and italics to the comment and select Add Comment
    Then the comment is saved with the typography elements displayed
    And can be viewed immediately along with the created by userid and creation date