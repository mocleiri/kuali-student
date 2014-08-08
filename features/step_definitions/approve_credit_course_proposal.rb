Given(/^I have a course proposal with approve fields partially completed created as (.*?)$/) do |proposal_author|
  log_in proposal_author, proposal_author

  if proposal_author == "fred"
  @course_proposal = create CmCourseProposalObject, :create_new_proposal => true,
                                                    :submit_fields => [(make CmSubmitFieldsObject, :subject_code => "ENGL" )],
                                                    :approve_fields => [(make CmApproveFieldsObject, :course_number => nil , :campus_location => nil)]
  elsif proposal_author == "alice"
    @course_proposal = create CmCourseProposalObject, :create_new_proposal => true,
                                                      :curriculum_review_process => "Yes",
                                                      :submit_fields => [(make CmSubmitFieldsObject, :subject_code => "ENGL" )],
                                                      :approve_fields => [(make CmApproveFieldsObject, :course_number => nil, :campus_location => nil)]
  end

  puts @course_proposal.proposal_title
  @course_proposal.submit_proposal
end

When(/^I am logged in as a (.*?)$/) do |reviewer|
  determine_reviewer(@course_proposal.submit_fields[0].subject_code)
end


And(/^I attempt to approve the course proposal as (.*?)$/) do |reviewer|
  log_in reviewer,reviewer
  navigate_rice_to_cm_home
  @course_proposal.search(@course_proposal.proposal_title)

end

And(/^I review the proposal as (.*?)$/) do |reviewer|
  log_in reviewer,reviewer
  navigate_rice_to_cm_home
  @course_proposal.search(@course_proposal.proposal_title)
  @course_proposal.review_proposal_action
end





=begin
And(/^I attempt to approve or acknowledge the course proposal as (.*?)$/) do
  log_in reviewer,reviewer
  navigate_rice_to_cm_home
  @course_proposal.search(@course_proposal.proposal_title)
end
=end


Then(/^I cannot approve or acknowledge the incomplete proposal$/) do
  @course_proposal.review_proposal_action
  #TODO Add validation to check that Approve is disabled.
end


And(/^I approve the course proposal as (.*?)$/) do |reviewer|
  log_in reviewer, reviewer
  navigate_rice_to_cm_home
  @course_proposal.search(@course_proposal.proposal_title)
  @course_proposal.review_proposal_action
  @course_proposal.approve_proposal
end


Then(/^I cannot approve the incomplete proposal$/) do
  @course_proposal.review_proposal_action
    on CmReviewProposal do |proposal|
      proposal.approve_button_disabled.exists?.should be_true #currently has a bug fixed by KSCM-1648
    end
end

Given(/^I have a course proposal with approve fields created as (.*?)$/) do |proposal_author|
  log_in proposal_author, proposal_author

  if proposal_author == "fred"

  @course_proposal = create CmCourseProposalObject, :create_new_proposal => true,
                                                    :submit_fields => [(make CmSubmitFieldsObject, :subject_code => "CHEM")],
                                                    :approve_fields => [(make CmApproveFieldsObject, :course_number => "#{(900..999).to_a.sample}")]

  elsif proposal_author == "alice"
    @course_proposal = create CmCourseProposalObject, :create_new_proposal => true,
                                                      :curriculum_review_process => "Yes",
                                                      :submit_fields => [(make CmSubmitFieldsObject, :subject_code => "CHEM")],
                                                      :approve_fields => [(make CmApproveFieldsObject, :course_number => "#{(900..999).to_a.sample}" )]
  end

  puts @course_proposal.proposal_title
  @course_proposal.submit_proposal
end



Given(/^I have a partial completed course proposal created as (.*?)$/) do |proposal_author|
  log_in proposal_author,proposal_author

  if proposal_author == "fred"

    @course_proposal = create CmCourseProposalObject, :create_new_proposal => true,
                              :submit_fields => [(make CmSubmitFieldsObject, :subject_code => "ENGL")],
                              :approve_fields => [(make CmApproveFieldsObject, :course_number => nil)]

  elsif proposal_author == "alice"
    @course_proposal = create CmCourseProposalObject, :create_new_proposal => true,
                              :curriculum_review_process => "Yes",
                              :submit_fields => [(make CmSubmitFieldsObject, :subject_code => "ENGL")],
                              :approve_fields => [(make CmApproveFieldsObject, :course_number => nil)]
  end

  puts "Proposal Title: #{@course_proposal.proposal_title}"
  puts "Course Title: #{@course_proposal.course_title}"
  @course_proposal.submit_proposal

end


And(/^I complete the missing fields on the proposal and approve as (.*?)$/) do

  @course_proposal.edit :defer_save => true
  @course_proposal.approve_fields[0].edit :course_number => "#{(900..999).to_a.sample}",
                                          :defer_save => false

  on(CmCourseInformation).review_proposal
  @course_proposal.approve_proposal

end

Then(/^missing fields are highlighted and proposal cannot be approved$/) do
    on CmReviewProposal do |review_proposal|
      review_proposal.course_number_review_error_state.exists?.should be_true
      review_proposal.approve_button_disabled.exists?.should be_true
    end
end

And(/^I approve the completed course proposal$/) do

end


Then(/^the course proposal is successfully approved$/) do
  steps %{Given I am logged in as Curriculum Specialist}
  navigate_rice_to_cm_home
  @course_proposal.search(@course_proposal.proposal_title)
  @course_proposal.review_proposal_action
  on CmReviewProposal do |review|
    review.proposal_status.should include "Approved"
  end
end

Then(/^I see successful approve messaging$/) do
on CmReviewProposal do |review|
  review.growl_text.should == "Document was successfully approved"
end
end

And(/^the new course is Active$/) do
  steps %{Given I am logged in as Faculty}
  @course = make CmCourseObject, :search_term => "#{@course_proposal.submit_fields[0].subject_code}#{@course_proposal.approve_fields[0].course_number}",
                                 :course_state => "Active"
  @course.search_for_course
  @course.view_course
  on CmReviewProposal do |course_review|
    course_review.course_state_review.capitalize.should include @course.course_state #has a bug KSCM-2564
    #COURSE INFORMATION
    course_review.course_title_review.should include @course.course_title
    "#{course_review.subject_code_review}""#{course_review.course_number_review}".should include @course.course_code
    course_review.description_review.should include @course.description
  end
end


Given(/^I have a credit course proposal with approve fields partially completed created as (.*?)/) do |author|
  log_in author,author
  @course_proposal = create CmCourseProposalObject, :create_new_proposal => true,
                            :submit_fields => [(make CmSubmitFieldsObject, :subject_code => "ENGL")],
                            :approve_fields => [(make CmApproveFieldsObject, :course_number => "#{(900..999).to_a.sample}",
                                                                             :transcript_course_title => nil,
                                                                             :campus_location => nil)]

end


When(/^I approve and activate the proposal$/) do
  @course_proposal.approve_activate_proposal
end


Then(/^missing fields are highlighted and proposal cannot be approved or activated$/) do
 on CmCourseInformation do |proposal|
   #add code to validate that approve and activate is disabled
   #add code to validate the missing fields error message
 end
end


Given(/^I have a credit course proposal with approve fields completed created as (.*?)/) do |author|
  log_in author,author
  @course_proposal = create CmCourseProposalObject, :create_new_proposal => true,
                            :submit_fields => [(make CmSubmitFieldsObject, :subject_code => "ENGL")],
                            :approve_fields => [(make CmApproveFieldsObject, :course_number => "#{(900..999).to_a.sample}" )]


end

Then(/^the proposal is successfully approved$/) do
  steps %{Given I am logged in as Curriculum Specialist}
  navigate_rice_to_cm_home
  @course_proposal.search(@course_proposal.proposal_title)
  @course_proposal.review_proposal_action
  on CmReviewProposal do |proposal|
    proposal.proposal_status.should == "Approved"
  end
end