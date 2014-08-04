When(/^I create a proposal with all fields populated$/) do
  @course_proposal1 = create CmCourseProposalObject, :create_new_proposal => true,
                            :proposal_title => random_alphanums(10,'approve and submit test data'),
                            :subject_code => ["ENGL","CHEM"].to_a.sample,
                            :submit_fields => [(make CmSubmitFieldsObject)],
                            :approve_fields => [(make CmApproveFieldsObject)]
end

Then(/^the proposal is successfully created$/) do
 puts "Proposal Title is: #{@course_proposal1.proposal_title}"
end