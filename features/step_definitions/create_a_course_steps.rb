#-----
# S1
#-----
plus_minus = "A-F with Plus/Minus Grading"
completed_notation = "Accepts a completed notation"
letter = "Letter"
pass_fail= "Pass/Fail Grading"
percentage = "Percentage Grading 0-100%"
satisfactory = "Administrative Grade of Satisfactory"
standard_exam = "Standard Final Exam"
alternate_exam = "Alternate Final Assessment"
no_exam = "No Final Exam or Assessment"


When /^I create a course proposal$/ do
  @course_proposal = create CmCourseProposalObject, :create_new_proposal => false
end

When /^I create a course proposal from blank$/ do
  @course_proposal = create CmCourseProposalObject, :save_proposal => false, :proposal_title => nil, :course_title =>nil
end

Then /^I should see a blank course proposal$/ do
  on CmCourseInformation do |page|
    page.proposal_title.text.should == ""
    page.course_title.text.should == ""
  end
  end

And /^I cancel create a course$/ do
  @course_proposal.cancel_create_course
end

And /^I cancel the course proposal page$/ do
  @course_proposal.cancel_course_proposal
end

Then /^I should see CM Home$/ do
  on CmCurriculum do |page|
    page.cmcurriculum_header.exists?.should == true
  end
end


Then /^I should see data in the proposal title on course information$/ do
  on CmCourseInformation do |page|
    page.course_information
    page.proposal_title.value.should == @course_proposal.proposal_title
    #TODO:: add in validation for assessment_scale
  end
end

And /^I should see data in the course title on course information$/ do
  on CmCourseInformation do |page|
    page.course_information
    page.course_title.value.should == @course_proposal.course_title
  end
end

When /^I complete the required fields for save on the new course proposal$/ do
  @course_proposal = create CmCourseProposalObject
end

When /^I complete the required for save fields on the course proposal and save$/ do
  @course_proposal = create CmCourseProposalObject, :curriculum_review_process => "Yes"
end


When /^I complete the required fields on the course admin proposal$/ do
  @course_proposal = create CmCourseProposalObject
end


Then /^I should see data in required fields for the (.*?)$/ do |proposal_type|


  on(CmCourseInformation).course_information

  on CmCourseInformation do |page|
    page.proposal_title.value.should == @course_proposal.proposal_title
    page.course_title.value.should == @course_proposal.course_title
    page.growl_text.should == "Document was successfully saved."
    page.page_header_text.should == "#{@course_proposal.proposal_title} (Admin Proposal)" if proposal_type == "admin proposal"
    page.page_header_text.should == "#{@course_proposal.proposal_title} (Proposal)" if proposal_type == "course proposal"
    page.transcript_course_title.value.should == @course_proposal.transcript_course_title
    page.subject_code.value.should == @course_proposal.subject_code
    page.course_number.value.should == @course_proposal.course_number
    page.description_rationale.value.should == @course_proposal.description_rationale
    page.proposal_rationale.value.should == @course_proposal.proposal_rationale
  end

  on CmGovernance do |page|
    page.governance
    page.location_north.should be_checked if @course_proposal.location_north == :set
    page.location_south.should be_checked if @course_proposal.location_south == :set
    page.location_extended.should be_checked if @course_proposal.location_extended == :set
    page.location_all.should be_checked if @course_proposal.location_all == :set
    page.curriculum_oversight_when_added(@course_proposal.curriculum_oversight).should be_present
  end

  on CmCourseLogistics do |page|
    page.course_logistics

    #GRADES AND ASSESSMENTS
    page.assessment_a_f.should be_checked if @course_proposal.assessment_a_f == :set
    page.assessment_notation.should be_checked if @course_proposal.assessment_notation == :set
    page.assessment_letter.should be_checked if @course_proposal.assessment_letter == :set
    page.assessment_pass_fail.should be_checked if @course_proposal.assessment_pass_fail == :set
    page.assessment_percentage.should be_checked if @course_proposal.assessment_percentage== :set
    page.assessment_satisfactory.should be_checked if @course_proposal.assessment_satisfactory == :set

    #FINAL EXAM
    page.exam_standard.should be_checked unless @course_proposal.exam_standard.nil?
    page.exam_alternate.should be_checked  unless @course_proposal.exam_alternate.nil?
    page.exam_none.should be_checked unless @course_proposal.exam_none.nil?
    page.final_exam_rationale.value.should == @course_proposal.final_exam_rationale unless page.exam_standard.set?

    #OUTCOMES DISABLED TEMPORARILY UNTIL OUTCOME COLLECTION IS FIXED May 23, 2014 - AR
    #page.credit_value(0).value.should == "#{@course_proposal.outcome_list[0].credit_value}" if @course_proposal.outcome_list[0].outcome_type == "Fixed"
    #page.credit_value(1).value.should == "#{@course_proposal.outcome_list[1].credit_value}" if @course_proposal.outcome_list[1].outcome_type == "Range"
    #page.credit_value(2).value.should == "#{@course_proposal.outcome_list[2].credit_value}" if @course_proposal.outcome_list[2].outcome_type == "Multiple"


    #FORMATS
    page.type_added(1,1).selected?(@course_proposal.format_list[0].type).should == true
    page.contacted_hours_added(1,1).should == "#{@course_proposal.format_list[0].contacted_hours}"
    page.frequency_added(1,1).selected?(@course_proposal.format_list[0].contact_frequency).should == true
    page.duration_type_added(1,1).selected?(@course_proposal.format_list[0].duration_type).should == true
    page.duration_count_added(1,1).should == "#{@course_proposal.format_list[0].duration_count}"
    page.class_size_added(1,1).should == "#{@course_proposal.format_list[0].class_size}"



  end

  on CmActiveDates do |page|
    page.active_dates
    page.start_term.selected?(@course_proposal.start_term).should == true unless @course_proposal.start_term.nil?
    #page.pilot_course.should be_checked
    #page.end_term.selected?(@course_proposal.end_term).should == true unless @course_proposal.end_term.nil?
  end

end

When /^I am on the course information page and I click save progress without entering any values$/ do
  @course_proposal = create CmCourseProposalObject, :proposal_title => nil, :course_title => nil
end

And /^I click the save progress button$/ do

end

Then /^I should receive an error message about the proposal title and course title being required for save$/ do
 on CmCourseInformation do |page|
   #page.course_information
   page.proposal_title_error_state.exists?.should == true
   page.course_title_error_state.exists?.should == true
   page.growl_text.should include "The form contains errors. Please correct these errors and try again."
   page.page_validation_header.should include "This page has 2 errors"
 end
end

Then /^I should see data in required for save fields for the course proposal$/ do
  on CmCourseInformation do |page|
    page.course_information
    page.proposal_title.value.should == @course_proposal.proposal_title
    page.course_title.value.should == @course_proposal.course_title
    page.growl_text.should == "Document was successfully saved."
  end
end



And /^I should see data in required for save fields on the Review Proposal page$/ do
  on CmCourseInformation do |page|
    page.review_proposal
    page.loading_wait

  end
 
  on CmReviewProposal do |page|
    #puts "Original Proposal Title is #{page.proposal_title_review}"
    #puts "Original Course Title is #{page.course_title_review}"
    page.proposal_title_review.should == @course_proposal.proposal_title
    page.course_title_review.should == @course_proposal.course_title
  end
end

And /^I edit the required for save fields and save$/ do
   @course_proposal.edit :proposal_title => "updated #{random_alphanums(10,'test proposal title ')}",
                         :course_title => "updated #{random_alphanums(10, 'test course title ')}",
                         :outcome_type_1 => nil,
                         :outcome_type_2 => nil,
                         :outcome_type_3 => nil,
                         :start_term => nil
end

And /^I edit the course proposal$/ do
  @course_proposal.edit   :proposal_title => "updated #{random_alphanums(10,'test proposal title ')}",
                          :course_title => "updated #{random_alphanums(10, 'test course title ')}",
                          :transcript_course_title => "updated #{random_alphanums(1,'123')}",
                          :subject_code => "ENGL",
                          :course_number => (100..999).to_a.sample,
                          :description_rationale => "updated #{random_alphanums(20, 'test description rationale ')}",
                          :proposal_rationale => "updated #{random_alphanums(20, 'test proposal rationale ')}",
                          :campus_location => [:location_all, :location_extended, :location_north, :location_south],
                          :curriculum_oversight => '::random::',
                          :assessment_scale => [:assessment_a_f, :assessment_notation, :assessment_letter, :assessment_pass_fail, :assessment_percentage, :assessment_satisfactory],
                          :final_exam_type => [:exam_standard, :exam_alternate, :exam_none],
                          :final_exam_rationale => "updated #{random_alphanums(10,'test final exam rationale ')}",
                          :start_term => '::random::'

  @course_proposal.outcome_list[0].delete
  @course_proposal.outcome_list[1].edit :credit_value=>"#{(1..4).to_a.sample}-#{(5..9).to_a.sample}",
                                        :outcome_level => 1
  @course_proposal.add_outcome :outcome => (make CmOutcomeObject,:outcome_type => "Fixed",:outcome_level => 2, :credit_value => 5)
  @course_proposal.format_list[0].edit :format_level => 1,
                                       :activity_level => 1,
                                       :type => '::random::',
                                       :contacted_hours => (1..9).to_a.sample,
                                       :contact_frequency => '::random::',
                                       :duration_count => (1..9).to_a.sample,
                                       :duration_type => '::random::',
                                       :class_size => (1..9).to_a.sample

end

And /^I edit the course proposal for Faculty$/ do
  @course_proposal_faculty.edit :proposal_title => "updated #{random_alphanums(10,'test proposal title ')}", :course_title => "updated #{random_alphanums(10, 'test course title ')}"
end

Then /^I should not see the edit option in the search results for the Course Admin Proposal$/ do
  on FindProposalPage do |page|
    page.edit_proposal_element(@course_proposal.proposal_title).exists?.should == false
  end
end

And /^I should see the updated data on the Review proposal page$/ do
  on CmCourseInformation do |page|
    page.review_proposal
    page.loading_wait
  end

  on CmReviewProposal do |page|
    #COURSE INFORMATION SECTION
    page.proposal_title_review.should == @course_proposal.proposal_title
    page.course_title_review.should == @course_proposal.course_title
    page.subject_code_review.should == "#{@course_proposal.subject_code}"
    page.description_review.should == "#{@course_proposal.description_rationale}"
    page.proposal_rationale_review.should == "#{@course_proposal.proposal_rationale}"

    #GOVERNANCE SECTION
    page.curriculum_oversight_review.should == @course_proposal.curriculum_oversight unless @course_proposal.curriculum_oversight.nil?

    #COURSE LOGISTICS SECTION
    #ASSESSMENT SCALE


    page.assessment_scale_review.should == plus_minus if @course_proposal.assessment_a_f == :set
    page.assessment_scale_review.should == completed_notation if @course_proposal.assessment_notation == :set
    page.assessment_scale_review.should == letter if @course_proposal.assessment_letter == :set
    page.assessment_scale_review.should == pass_fail if @course_proposal.assessment_pass_fail == :set
    page.assessment_scale_review.should == percentage if @course_proposal.assessment_percentage == :set
    page.assessment_scale_review.should == satisfactory if @course_proposal.assessment_satisfactory == :set

    #FINAL EXAM
    page.final_exam_status_review.should == standard_exam unless @course_proposal.exam_standard.nil?
    page.final_exam_status_review.should == alternate_exam unless @course_proposal.exam_alternate.nil?
    page.final_exam_status_review.should == no_exam unless @course_proposal.exam_none.nil?
    page.final_exam_rationale_review.should == @course_proposal.final_exam_rationale unless @course_proposal.exam_standard == :set

    #FIXED OUTCOME
    page.outcome_level_review(1).should == "Outcome #{@course_proposal.outcome_list[0].outcome_level.to_i+1}" unless @course_proposal.outcome_list[0].outcome_level.nil?
    page.outcome_type_review(1).should == "Fixed" unless @course_proposal.outcome_list[0].outcome_type.nil?
    page.outcome_credit_review(1) == "#{@course_proposal.outcome_list[0].credit_value}" unless @course_proposal.outcome_list[0].credit_value.nil?

    #RANGE OUTCOME
    page.outcome_level_review(2).should == "Outcome #{@course_proposal.outcome_list[1].outcome_level.to_i+1 }" unless @course_proposal.outcome_list[1].outcome_level.nil?
    page.outcome_type_review(2).should == "Multiple" unless @course_proposal.outcome_list[1].outcome_type.nil?
    page.outcome_credit_review(2) == "#{@course_proposal.outcome_list[1].credit_value}" unless @course_proposal.outcome_list[0].credit_value.nil?

    #MULTIPLE OUTCOME
    page.outcome_level_review(3).should == "Outcome #{@course_proposal.outcome_list[2].outcome_level.to_i+1}" unless @course_proposal.outcome_list[1].outcome_level.nil?
    page.outcome_type_review(3).should == "Range" unless @course_proposal.outcome_list[2].outcome_type.nil?
    page.outcome_credit_review(3) == "#{@course_proposal.outcome_list[2].credit_value}" unless @course_proposal.outcome_list[0].credit_value.nil?
    
    #ACTIVITY FORMAT
    page.activity_level_review(1).should == "Format 1"
    page.activity_type_review(1).should include "#{@course_proposal.format_list[0].type}".gsub(/\s+/, "")
    page.activity_contact_hours_frequency_review(1).should include "#{@course_proposal.format_list[0].contacted_hours}"
    page.activity_contact_hours_frequency_review(1).should include "#{@course_proposal.format_list[0].contact_frequency}"
    page.activity_duration_type_count_review(1).should include "#{@course_proposal.format_list[0].duration_type}"
    page.activity_duration_type_count_review(1).should include "#{@course_proposal.format_list[0].duration_count}"
    page.activity_class_size_review(1).should == "#{@course_proposal.format_list[0].class_size}"

    #ACTIVE DATES SECTION
    page.start_term_review.should == @course_proposal.start_term unless @course_proposal.start_term.nil?

  end

end

And /^I should see updated data on Review proposal page$/ do
  on CmCourseInformation do |page|
    page.review_proposal
    page.loading_wait
  end

  on CmReviewProposal do |page|
    #COURSE INFORMATION SECTION
    page.proposal_title_review.should == @course_proposal.proposal_title
    page.course_title_review.should == @course_proposal.course_title
    page.transcript_course_title.should == @course_proposal.transcript_course_title
    page.subject_code_review.should == "#{@course_proposal.subject_code}"
    page.course_number_review.should == "#{@course_proposal.course_number}"
    page.description_review.should == "#{@course_proposal.description_rationale}"
    page.proposal_rationale_review.should == "#{@course_proposal.proposal_rationale}"

    #GOVERNANCE SECTION
    page.curriculum_oversight_review.should == @course_proposal.curriculum_oversight unless @course_proposal.curriculum_oversight.nil?

    #COURSE LOGISTICS SECTION
    #ASSESSMENT SCALE
    page.assessment_scale_review.should == plus_minus if @course_proposal.assessment_a_f == :set
    page.assessment_scale_review.should == completed_notation if @course_proposal.assessment_notation == :set
    page.assessment_scale_review.should == letter if @course_proposal.assessment_letter == :set
    page.assessment_scale_review.should == pass_fail if @course_proposal.assessment_pass_fail == :set
    page.assessment_scale_review.should == percentage if @course_proposal.assessment_percentage == :set
    page.assessment_scale_review.should == satisfactory if @course_proposal.assessment_satisfactory == :set

    #FINAL EXAM
    page.final_exam_status_review.should == standard_exam unless @course_proposal.exam_standard.nil?
    page.final_exam_status_review.should == alternate_exam unless @course_proposal.exam_alternate.nil?
    page.final_exam_status_review.should == no_exam unless @course_proposal.exam_none.nil?
    page.final_exam_rationale_review.should == @course_proposal.final_exam_rationale unless @course_proposal.exam_standard == :set

    #FIXED OUTCOME
    page.outcome_type_review(1).should == "Fixed" unless @course_proposal.outcome_list[3].outcome_type.nil?
    page.outcome_credit_review(1) == "#{@course_proposal.outcome_list[3].credit_value}" unless @course_proposal.outcome_list[3].credit_value.nil?

    #RANGE OUTCOME
    page.outcome_type_review(2).should == "Multiple" unless @course_proposal.outcome_list[1].outcome_type.nil?
    page.outcome_credit_review(2) == "#{@course_proposal.outcome_list[1].credit_value}" unless @course_proposal.outcome_list[1].credit_value.nil?

    #MULTIPLE OUTCOME
    page.outcome_type_review(3).should == "Range" unless @course_proposal.outcome_list[2].outcome_type.nil?
    page.outcome_credit_review(3) == "#{@course_proposal.outcome_list[2].credit_value}" unless @course_proposal.outcome_list[2].credit_value.nil?


    #ACTIVE DATES SECTION
    page.start_term_review.should == @course_proposal.start_term unless @course_proposal.start_term.nil?

  end


end


And /^I should see updated data on the Review proposal page$/ do
  navigate_to_cm_home
  @course_proposal.search(@course_proposal.proposal_title)
  @course_proposal.review_proposal_action

  on CmReviewProposal do |page|
    #COURSE INFORMATION SECTION
    page.proposal_title_review.should == @course_proposal.proposal_title
    page.course_title_review.should == @course_proposal.course_title
    page.transcript_course_title.should == @course_proposal.transcript_course_title
    page.subject_code_review.should == "#{@course_proposal.subject_code}"
    page.course_number_review.should == "#{@course_proposal.course_number}"
    page.description_review.should == "#{@course_proposal.description_rationale}"
    page.proposal_rationale_review.should == "#{@course_proposal.proposal_rationale}"

    #GOVERNANCE SECTION
    page.curriculum_oversight_review.should == @course_proposal.curriculum_oversight unless @course_proposal.curriculum_oversight.nil?

    #COURSE LOGISTICS SECTION
    #ASSESSMENT SCALE
    page.assessment_scale_review.should == plus_minus if @course_proposal.assessment_a_f == :set
    page.assessment_scale_review.should == completed_notation if @course_proposal.assessment_notation == :set
    page.assessment_scale_review.should == letter if @course_proposal.assessment_letter == :set
    page.assessment_scale_review.should == pass_fail if @course_proposal.assessment_pass_fail == :set
    page.assessment_scale_review.should == percentage if @course_proposal.assessment_percentage == :set
    page.assessment_scale_review.should == satisfactory if @course_proposal.assessment_satisfactory == :set

    #FINAL EXAM
    page.final_exam_status_review.should == standard_exam unless @course_proposal.exam_standard.nil?
    page.final_exam_status_review.should == alternate_exam unless @course_proposal.exam_alternate.nil?
    page.final_exam_status_review.should == no_exam unless @course_proposal.exam_none.nil?
    page.final_exam_rationale_review.should == @course_proposal.final_exam_rationale unless @course_proposal.exam_standard == :set

    #FIXED OUTCOME
    page.outcome_type_review(1).should == "Fixed" unless @course_proposal.outcome_list[3].outcome_type.nil?
    page.outcome_credit_review(1) == "#{@course_proposal.outcome_list[3].credit_value}" unless @course_proposal.outcome_list[3].credit_value.nil?

    #RANGE OUTCOME
    page.outcome_type_review(2).should == "Multiple" unless @course_proposal.outcome_list[1].outcome_type.nil?
    page.outcome_credit_review(2) == "#{@course_proposal.outcome_list[1].credit_value}" unless @course_proposal.outcome_list[1].credit_value.nil?

    #MULTIPLE OUTCOME
    page.outcome_type_review(3).should == "Range" unless @course_proposal.outcome_list[2].outcome_type.nil?
    page.outcome_credit_review(3) == "#{@course_proposal.outcome_list[2].credit_value}" unless @course_proposal.outcome_list[2].credit_value.nil?


    #ACTIVITY FORMAT
    page.activity_level_review(1).should == "Format #{@course_proposal.format_list[0].format_level}"
    page.activity_type_review(1).should include "#{@course_proposal.format_list[0].type}".gsub(/\s+/, "")
    page.activity_contact_hours_frequency_review(1).should include "#{@course_proposal.format_list[0].contacted_hours}"
    page.activity_contact_hours_frequency_review(1).should include "#{@course_proposal.format_list[0].contact_frequency}"
    page.activity_duration_type_count_review(1).should include "#{@course_proposal.format_list[0].duration_type}"
    page.activity_duration_type_count_review(1).should include "#{@course_proposal.format_list[0].duration_count}"
    page.activity_class_size_review(1).should == "#{@course_proposal.format_list[0].class_size}"


    #ACTIVE DATES SECTION
    page.start_term_review.should == @course_proposal.start_term unless @course_proposal.start_term.nil?

  end

end


And /^I should see updated data on the Review proposal page for course (.*?)$/ do |proposal_type|
  if proposal_type == "proposal"
    on CmCourseInformation do |page|
      page.review_proposal
      page.loading_wait
    end

    on CmReviewProposal do |page|
      page.growl_text.should == "Document was successfully saved."
      page.proposal_title_review.should == @course_proposal_faculty.proposal_title
      page.course_title_review.should == @course_proposal_faculty.course_title
    end
  else
    on CmCourseInformation do |page|
      page.review_proposal
      page.loading_wait
      page.growl_text.should == "Document was successfully saved."
    end
    on CmReviewProposal do |page|
      page.proposal_title_review.should == @course_proposal_cs.proposal_title
      page.course_title_review.should == @course_proposal_cs.course_title
    end
  end
end

When /^I complete the required fields on the course proposal$/ do
  @course_proposal = create CmCourseProposalObject

end

When(/^I create a proposal with alternate identifier details$/) do
  @course_proposal = create CmCourseProposalObject, subject_code: nil,
                                                   course_number: nil,
                                                   cross_listed_course_list:   [(make CmCrossListedObject, :auto_lookup => true), (make CmCrossListedObject, :cross_list_course_count => 2)],
                                                   jointly_offered_course_list: [(make CmJointlyOfferedObject, :auto_lookup => true)],
                                                                                 #TODO Disabled until KSCM-2195 dev issue is resolved
                                                                                 #(make CmJointlyOfferedObject, :jointly_offered_course_count => 2, :search_type => "name")
                                                                                 #(make CmJointlyOfferedObject, :jointly_offered_course_count => 3, :search_type => "course code"),
                                                                                 #(make CmJointlyOfferedObject, :jointly_offered_course_count => 4, :search_type => "plain text"),
                                                                                 #(make CmJointlyOfferedObject, :jointly_offered_course_count => 5, :search_type => "blank"),

                                                   version_code_list:           [(make CmVersionCodeObject), (make CmVersionCodeObject, :version_code_count => 2) ],
                                                   transcript_course_title: nil,
                                                   description_rationale: nil,
                                                   proposal_rationale: nil,
                                                   campus_location: nil,
                                                   curriculum_oversight: nil,
                                                   assessment_scale: nil,
                                                   final_exam_type: nil,
                                                   final_exam_rationale: nil,
                                                   outcome_list: nil,
                                                   format_list: nil,
                                                   start_term:nil

end


Then(/^I should see alternate identifier details on the course proposal$/) do
  @course_proposal.review_proposal_action

  on CmReviewProposal do |page|

    page.proposal_title_review.should == @course_proposal.proposal_title
    page.course_title_review.should == @course_proposal.course_title

    #Cross Listed Courses
    @course_proposal.cross_listed_course_list.each do |cross_list|
      page.cross_listed_courses_review.should include cross_list.cross_list_subject_code
    end

    #Jointly Offered Courses
    @course_proposal.jointly_offered_course_list.each do |jointly_offered|
      page.jointly_offered_courses_review.should include jointly_offered.jointly_offered_course
    end

    #Version Code
    @course_proposal.version_code_list.each do |version|
      page.version_codes_review.should include version.version_code
      page.version_codes_review.should include version.version_course_title
    end

  end

end

#-----
# S2
#-----



=begin
When /^I complete the required fields on the course proposal$/ do

  # Change to 'I complete the fields required to submit the course proposal'



  #@course_proposal.KradCourseProposalRequired
  #@course_proposal.create_course_proposal_required
  @course_proposal  = create CmCourseProposalObject,
                           #REQUIRED
                           #COURSE INFORMATION
                           #proposal_title: random_alphanums(10,'test proposal title '),
                           #course_title: random_alphanums(10, 'test course title'),
                           subject_code: 'MATH',
                           course_number: rand(100..999).to_s,
                           transcript_course_title:  random_alphanums(5,'test transcript'),

                           joint_offering_adding_method: ['adv_given_name', 'adv_course_code', 'adv_plain_text', 'auto_lookup'].sample,
                           instructor_adding_method:   ['auto_lookup', 'advanced', 'advanced_username', 'advanced_name'].sample,
                           description_rationale: random_alphanums(200),
                           proposal_rationale:    random_alphanums(200),
                           #GOVERNANCE
                           curriculum_oversight:  'CMNS-Mathematics',
                           #COURSE LOGISTICS
                           scheduling_term:      [:term_any, :term_fall, :term_spring, :term_summer],
                           final_exam_status:    [:exam_standard, :exam_alternate, :exam_none],
                           final_exam_rationale: random_alphanums(50),
                           outcome_type:       '::random::', #['Fixed', 'Multiple', 'Range'].sample,
                           outcome_value:      rand(1..3).to_s,
                           credit_value_min:   rand(1..3).to_s,
                           credit_value_max:   rand(4..8).to_s,
                           outcome_multiple:   rand(1..3).to_s,
                           outcome_multiple2:  rand(4..7).to_s,

                           activity_duration_type: '::random::', #['Day', 'Four Years', 'Half Semester', 'Hours', 'Mini-mester', 'Minutes', 'Month', 'Period', 'Quarter', 'Semester', 'Session', 'TBD', 'Term', 'Two Years', 'Week', 'Year'].sample,
                           activity_type: '::random::', #['Directed', 'Discussion', 'Experiential Learning/Other', 'Homework', 'Lab', 'Lecture', 'Tutorial', 'Web Discuss', 'Web Lecture'].sample,
                           activity_frequency: '::random::', #['per day', 'per month', 'per week'].sample,
                           activity_contacted_hours: rand(1..9).to_s,
                           activity_duration_count: rand(1..9).to_s,
                           activity_class_size: rand(1..9).to_s,

                           #ACTIVE DATES
                           start_term: 'Spring 1980',
                           pilot_course: :set,
                           end_term: 'Fall 1980'
end
=end



=begin
Then /^I should see data in required fields for the course proposal$/ do
  on(CmCourseInfo).course_information

  on CmCourseInfo do |page|
    page.subject_code.value.should == @course_proposal.subject_code
    page.course_number.value.should == @course_proposal.course_number

    #BUG KSCM-1240
    #page.version_code_version_code.value.should == @course_proposal.version_code_version_code
    #page.version_code_title.value.should == @course_proposal.version_code_title

    page.description_rationale.value.should == @course_proposal.description_rationale
    page.proposal_rationale.value.should == @course_proposal.proposal_rationale
  end

  on CmGovernance do |page|
    page.governance
    page.curriculum_oversight_when_added(@course_proposal.curriculum_oversight).should be_present
  end

  on CmLogistics do |page|
    page.logistics

    page.exam_standard.should be_checked unless @course_proposal.exam_standard.nil?
    page.exam_alternate.should be_checked  unless @course_proposal.exam_alternate.nil?
    page.exam_none.should be_checked unless @course_proposal.exam_none.nil?

    page.final_exam_rationale.value.should == @course_proposal.final_exam_rationale unless page.exam_standard.set?

    page.credit_value.value.should == @course_proposal.outcome_value if @course_proposal.outcome_type == 'Fixed'

    page.credit_value_multiple('0').value.should == @course_proposal.outcome_multiple if @course_proposal.outcome_type == 'Multiple'
    page.credit_value_multiple('1').value.should == @course_proposal.outcome_multiple2 if @course_proposal.outcome_type == 'Multiple'

    page.credit_value_min.value.should == @course_proposal.credit_value_min if @course_proposal.outcome_type == "Range"
    page.credit_value_max.value.should == @course_proposal.credit_value_max if @course_proposal.outcome_type == "Range"

    page.activity_type.selected?(@course_proposal.activity_type).should == true

    page.assessment_a_f.should be_checked if @course_proposal.assessment_a_f == :set
    page.assessment_notation.should be_checked if @course_proposal.assessment_notation == :set
    page.assessment_letter.should be_checked if @course_proposal.assessment_letter == :set
    page.assessment_pass_fail.should be_checked if @course_proposal.assessment_pass_fail == :set
    page.assessment_percentage.should be_checked if @course_proposal.assessment_percentage== :set
    page.assessment_satisfactory.should be_checked if @course_proposal.assessment_satisfactory == :set
  end

  on CmActiveDates do |page|
    page.active_dates
    page.start_term.selected?(@course_proposal.start_term).should == true unless @course_proposal.start_term.nil?
    page.pilot_course.should be_checked
    page.end_term.selected?(@course_proposal.end_term).should == true unless @course_proposal.end_term.nil?
  end
end
=end

#-----
# S3
#-----
Given /^I complete all the fields on the course proposal$/ do
  #@course_proposal.KradCourseProposalNonrequired
  #@course_proposal.course_proposal_nonrequired
  @course_proposal  = create CmCourseProposalObject,

                             subject_code: 'MATH',
                             course_number: rand(100..999).to_s,
                             transcript_course_title:  random_alphanums(5,'test transcript'),

                             joint_offering_adding_method: ['adv_given_name', 'adv_course_code', 'adv_plain_text', 'auto_lookup'].sample,
                             instructor_adding_method:   ['auto_lookup', 'advanced', 'advanced_username', 'advanced_name'].sample,
                             description_rationale: random_alphanums(200),
                             proposal_rationale:    random_alphanums(200),
                             #GOVERNANCE
                             curriculum_oversight:  'CMNS-Mathematics',
                             #COURSE LOGISTICS
                             scheduling_term:      [:term_any, :term_fall, :term_spring, :term_summer],
                             final_exam_status:    [:exam_standard, :exam_alternate, :exam_none],
                             final_exam_rationale: random_alphanums(50),
                             outcome_type:       '::random::', #['Fixed', 'Multiple', 'Range'].sample,
                             outcome_value:      rand(1..3).to_s,
                             credit_value_min:   rand(1..3).to_s,
                             credit_value_max:   rand(4..8).to_s,
                             outcome_multiple:   rand(1..3).to_s,
                             outcome_multiple2:  rand(4..7).to_s,

                             activity_duration_type: '::random::', #['Day', 'Four Years', 'Half Semester', 'Hours', 'Mini-mester', 'Minutes', 'Month', 'Period', 'Quarter', 'Semester', 'Session', 'TBD', 'Term', 'Two Years', 'Week', 'Year'].sample,
                             activity_type: '::random::', #['Directed', 'Discussion', 'Experiential Learning/Other', 'Homework', 'Lab', 'Lecture', 'Tutorial', 'Web Discuss', 'Web Lecture'].sample,
                             activity_frequency: '::random::', #['per day', 'per month', 'per week'].sample,
                             activity_contacted_hours: rand(1..9).to_s,
                             activity_duration_count: rand(1..9).to_s,
                             activity_class_size: rand(1..9).to_s,

                             #ACTIVE DATES
                             start_term: 'Spring 1980',
                             pilot_course: :set,
                             end_term: 'Fall 1980',

                             #COURSE INFORMATION
                             course_listing_subject: 'FREN',
                             course_listing_number: '888',
                             joint_offering_number: 'HIST201',
                             instructor_display_name: 'SMITH, DAVID (s.davidb)',
                             instructor_first_name: 'DAVID',
                             instructor_last_name: 'SMITH',
                             instructor_username: 's.davidb',

                             instructor_adding_method:   'auto_lookup',#['auto_lookup', 'advanced', adv_username', 'adv_name'].sample,
                             joint_offering_adding_data: ['adv_given_name', 'adv_course_code', 'adv_plain_text', 'auto_lookup'].sample,

                             joint_offering_name:        'Interpreting American History: From 1865 to the Present',
                             joint_offering_description: 'The United States from the end of the Civil War to the present.',
                             joint_offering_course_code: 'HIST201',

                             #GOVERNANCE
                             admin_org_adding_method:    ['advanced', 'auto_lookup'].sample,
                             administering_organization: 'Biological Sciences',
                             campus_location:            [:location_all, :location_extended, :location_north, :location_south],
                             #BUG FOR IDENTIFIER
                             # adv_admin_org_identifier: 'ORGID-BISI',
                             adv_admin_org_name:         'Biological Sciences',
                             adv_admin_org_abbreviation: 'BISI',

                             #COURSE LOGISTICS
                             duration_type: '::random::', #['Day', 'Four Years', 'Half Semester', 'Hours', 'Mini-mester', 'Minutes', 'Month', 'Period', 'Quarter', 'Semester', 'Session', 'TBD', 'Term', 'Two Years', 'Week', 'Year'].sample,
                             duration_count: rand(1..9).to_s,
                             audit: :set, pass_fail_transcript_grade: :set,

                             #COURSE REQUISITES

                             eligibility_add_method: 'text', #['text', 'advanced'].sample,
                             rule_adv_course_title: 'American Jewish Experience', rule_adv_course_code: 'HIST106', rule_adv_course_description_snip: 'History of the Jews in America',
                             rule_course_field: '', rule_credit: rand(1..4),

                             student_eligibility_rule: 'Must have successfully completed <course>',
                             corequisiste_rule: 'Must be concurrently enrolled in <course>',
                             recommended_preparation_rule: 'Must have successfully completed <course>',
                             antirequisite_rule: 'Must not have successfully completed <course>',
                             repeatable_for_credit_rule: 'May be repeated for a maximum of <n> credits',
                             course_that_restricts_credits_rule: 'Must not have successfully completed <course>',

                             student_eligibility_course: 'HIST110',
                             student_eligibility_title: 'The Ancient World',
                             student_eligibility_phrase: 'Interpretation of select literature and art of the ancient Mediterranean world with a view to illuminating the antecedents of modern culture; religion and myth in the ancient near East; Greek philosophical, scientific, and literary invention; and the Roman tradition in politics and administration.',

                             corequisite_add_method: ['text', 'advanced'],
                             corequisite_title: 'Germany in the Nineteenth Century, 1815-1914',
                             corequisite_course: 'HIST440',
                             corequisite_phrase: 'Examines the social, economic, cultural, and political development of the major German states before 1871 and of Germany, excluding Austria, from 1871 to 1914.',

                             recommended_preparation_course: 'CHEM277',
                             recommended_preparation_title: 'Fundamentals of Analytical and Bioanalytical Chemistry Laboratory',
                             recommended_preparation_phrase: 'Quantitative analysis, inorganic analytical chemistry, and an introduction to bio-analytical instrumentation and techniques.',

                             antirequisite_course: 'HIST453',
                             antirequisite_title: 'Diplomatic History of the United States from 1914',
                             antirequisite_phrase: 'American foreign relations in the 20th-century. World War I, the Great Depression, World War II, the Cold War, the Korean War, and Vietnam. A continuation of HIST452.',

                             repeatable_for_credit_credit: rand(1..5).to_s,

                             course_that_restricts_credits_course: 'HIST454',
                             course_that_restricts_credits_title: 'Constitutional History of the United States: From Colonial Origins to 1860',
                             course_that_restricts_credits_phrase: 'The interaction of government, law, and politics in the constitutional system. The nature and purpose of constitutions and constitutionalism; the relationship between the constitution and social forces and influences, the way in which constitutional principles, rules, ideas, and institutions affect events and are in turn affected by events. The origins of American politics and constitutionalism through the constitutional convention of 1787. Major constitutional problems such as the origins of judicial review, democratization of government, slavery in the territories and political system as a whole.',

                             corequisite_add_method: ['text', 'advanced'].sample,
                             recommended_preparation_add_method: ['text', 'advanced'].sample,
                             antirequisite_add_method: ['text', 'advanced'].sample,
                             course_that_restricts_credits_add_method: ['text', 'advanced'].sample,
                             student_eligibility_add_method: ['text', 'advanced'].sample,

  #AUTHORS & COLLABORATORS
                             author_name_method: ['auto_lookup', 'advanced_name', 'advanced_username'].sample,
                             author_name_search: 'User',author_username_search: 'user1', author_display_name: 'One, User (user1)',
                             author_permission: '::random::', #['Edit, Comments, View', 'Comments, View', 'View'].sample,
                             action_request: 'FYI',




                             author_notation: :set



end

Then /^I should see data in all non required fields for the course proposal$/ do
  on CmCourseInformation do |page|
    page.course_information
    page.transcript_course_title.value.should == @course_proposal.transcript_course_title
    #page.subject_code.value.should == @course_proposal.subject_code
    #@course_proposal.verify_text_field(page, 'course_listing_subject', 'course_listing_number', 'joint_offering_number')
    page.expand_course_listing_section unless page.collapse_course_listing_section.visible?
    page.course_listing_subject.value.should == @course_proposal.course_listing_subject
    page.course_listing_number.value.should == @course_proposal.course_listing_number
    page.joint_offering_number.value.should == @course_proposal.joint_offering_number
    page.added_instructor_name.value.should == @course_proposal.instructor_display_name
  end

  on CmGovernance do |page|
    page.governance
    #@course_proposal.verify_text_field(page, 'added_administering_organization' )
    page.added_administering_organization.value.should == @course_proposal.administering_organization unless @course_proposal.administering_organization.nil?
    page.location_north.should be_checked if @course_proposal.location_north == 'set'
    page.location_south.should be_checked if @course_proposal.location_south == 'set'
    page.location_extended.should be_checked if @course_proposal.location_extended == 'set'
    page.location_all.should be_checked if @course_proposal.location_all == 'set'
  end

  on CmCourseLogistics do |page|
    page.course_logistics
    page.term_any.should be_checked if @course_proposal.term_any == 'set'
    page.term_fall.should be_checked if @course_proposal.term_fall == 'set'
    page.term_spring.should be_checked if @course_proposal.term_spring == 'set'
    page.term_summer.should be_checked if @course_proposal.term_summer == 'set'

    page.duration_count.value.should == @course_proposal.duration_count unless @course_proposal.duration_count.nil?
    page.duration_type.selected?(@course_proposal.duration_type).should == true unless @course_proposal.duration_type.nil?

    page.audit.should be_checked
    page.pass_fail_transcript_grade.should be_checked

    #@course_proposal.verify_text_field(page, 'activity_contacted_hours', 'activity_duration_count', 'activity_class_size' )

    page.contacted_hours.value.should == @course_proposal.activity_contacted_hours
    page.frequency.selected?(@course_proposal.activity_frequency).should == true unless @course_proposal.activity_frequency.nil?
    page.duration_count.value.should == @course_proposal.activity_duration_count
    page.duration_type.selected?(@course_proposal.activity_duration_type).should == true unless @course_proposal.activity_duration_type.nil?
    page.class_size.value.should == @course_proposal.activity_class_size


  end
  #on KradLearningObjectives do |page|
  #  page.learning_objectives
  #end

  on CmCourseRequisites do |page|
    page.course_requisites

    page.course_requisite_added_rule(@course_proposal.student_eligibility_rule_with_value).should be_present unless @course_proposal.student_eligibility_rule.nil?
    page.course_requisite_added_rule(@course_proposal.corequisite_rule_with_value).should be_present unless @course_proposal.corequisite_rule.nil?

    #page.course_requisite_added_rule().should be_present unless @course_proposal.student_eligibility_prerequisite_rule.nil?
    #page.corequisite_added_rule.should be_present unless @course_proposal.corequisite_rule.nil?

    page.recommended_preparation_added_rule(@course_proposal.recommended_preparation_rule_with_value).should be_present unless @course_proposal.recommended_preparation_rule.nil?
    page.antirequisite_added_rule(@course_proposal.antirequisite_rule_with_value).should be_present unless @course_proposal.antirequisite_rule.nil?
    page.course_that_restricts_credits_added_rule(@course_proposal.course_that_restricts_credits_rule_with_value).should be_present unless @course_proposal.course_that_restricts_credits_rule.nil?

    page.repeatable_for_credit_added_rule(@course_proposal.repeatable_for_credit_rule_with_value).should be_present unless @course_proposal.repeatable_for_credit_rule.nil?

    #page.course_requisite_added_rule('lets fail here').should be_present

  end

    on CmAuthorsCollaborators do |page|
    page.authors_collaborators
    page.added_author_information('edit').should be_present if @course_proposal.author_permission == 'Edit, Comments, View'
    page.added_author_information('comment').should be_present if @course_proposal.author_permission == 'Comments, View'
    page.added_author_information('view').should be_present if @course_proposal.author_permission == 'View'

    page.added_author_information('fyi').should be_present if @course_proposal.action_request == 'FYI'
    page.added_author_information('false').should be_present if @course_proposal.author_notation.nil?
    page.added_author_information('true').should be_present unless @course_proposal.author_notation.nil?

    page.added_author_information(@course_proposal.author_display_name).should be_present
  end
end


#-----
# S4
#-----
When /^I complete all fields on the course proposal with advanced search$/ do
  @course_proposal  = create CmCourseProposalObject,

                             subject_code: 'MATH',
                             course_number: rand(100..999).to_s,
                             transcript_course_title:  random_alphanums(5,'test transcript'),

                             joint_offering_adding_method: ['adv_given_name', 'adv_course_code', 'adv_plain_text'].sample,
                             instructor_adding_method:   ['advanced', 'advanced_username', 'advanced_name'].sample,
                             description_rationale: random_alphanums(200),
                             proposal_rationale:    random_alphanums(200),
                             #GOVERNANCE
                             curriculum_oversight:  'CMNS-Mathematics',
                             #COURSE LOGISTICS
                             scheduling_term:      [:term_any, :term_fall, :term_spring, :term_summer],
                             final_exam_status:    [:exam_standard, :exam_alternate, :exam_none],
                             final_exam_rationale: random_alphanums(50),
                             outcome_type:       '::random::', #['Fixed', 'Multiple', 'Range'].sample,
                             outcome_value:      rand(1..3).to_s,
                             credit_value_min:   rand(1..3).to_s,
                             credit_value_max:   rand(4..8).to_s,
                             outcome_multiple:   rand(1..3).to_s,
                             outcome_multiple2:  rand(4..7).to_s,

                             activity_duration_type: '::random::', #['Day', 'Four Years', 'Half Semester', 'Hours', 'Mini-mester', 'Minutes', 'Month', 'Period', 'Quarter', 'Semester', 'Session', 'TBD', 'Term', 'Two Years', 'Week', 'Year'].sample,
                             activity_type: '::random::', #['Directed', 'Discussion', 'Experiential Learning/Other', 'Homework', 'Lab', 'Lecture', 'Tutorial', 'Web Discuss', 'Web Lecture'].sample,
                             activity_frequency: '::random::', #['per day', 'per month', 'per week'].sample,
                             activity_contacted_hours: rand(1..9).to_s,
                             activity_duration_count: rand(1..9).to_s,
                             activity_class_size: rand(1..9).to_s,

                             #ACTIVE DATES
                             start_term: 'Spring 1980',
                             pilot_course: :set,
                             end_term: 'Fall 1980',

                             #COURSE INFORMATION
                             course_listing_subject: 'FREN',
                             course_listing_number: '888',
                             joint_offering_number: 'HIST201',
                             instructor_display_name: 'SMITH, DAVID (s.davidb)',
                             instructor_first_name: 'DAVID',
                             instructor_last_name: 'SMITH',
                             instructor_username: 's.davidb',

                             instructor_adding_method:   ['advanced', 'adv_username', 'adv_name'].sample,
                             joint_offering_adding_data: ['adv_given_name', 'adv_course_code', 'adv_plain_text'].sample,

                             joint_offering_name:        'Interpreting American History: From 1865 to the Present',
                             joint_offering_description: 'The United States from the end of the Civil War to the present.',
                             joint_offering_course_code: 'HIST201',

                             #GOVERNANCE
                             admin_org_adding_method:    'advanced',
                             administering_organization: 'Biological Sciences',
                             campus_location:            [:location_all, :location_extended, :location_north, :location_south],
                             #BUG FOR IDENTIFIER
                             # adv_admin_org_identifier: 'ORGID-BISI',
                             adv_admin_org_name:         'Biological Sciences',
                             adv_admin_org_abbreviation: 'BISI',

                             #COURSE LOGISTICS
                             duration_type: '::random::', #['Day', 'Four Years', 'Half Semester', 'Hours', 'Mini-mester', 'Minutes', 'Month', 'Period', 'Quarter', 'Semester', 'Session', 'TBD', 'Term', 'Two Years', 'Week', 'Year'].sample,
                             duration_count: rand(1..9).to_s,
                             audit: :set, pass_fail_transcript_grade: :set,

                             #COURSE REQUISITES

                             eligibility_add_method: 'text', #['text', 'advanced'].sample,
                             rule_adv_course_title: 'American Jewish Experience', rule_adv_course_code: 'HIST106', rule_adv_course_description_snip: 'History of the Jews in America',
                             rule_course_field: '', rule_credit: rand(1..4),

                             student_eligibility_rule: 'Must have successfully completed <course>',
                             corequisiste_rule: 'Must be concurrently enrolled in <course>',
                             recommended_preparation_rule: 'Must have successfully completed <course>',
                             antirequisite_rule: 'Must not have successfully completed <course>',
                             repeatable_for_credit_rule: 'May be repeated for a maximum of <n> credits',
                             course_that_restricts_credits_rule: 'Must not have successfully completed <course>',

                             student_eligibility_course: 'HIST110',
                             student_eligibility_title: 'The Ancient World',
                             student_eligibility_phrase: 'Interpretation of select literature and art of the ancient Mediterranean world with a view to illuminating the antecedents of modern culture; religion and myth in the ancient near East; Greek philosophical, scientific, and literary invention; and the Roman tradition in politics and administration.',

                             corequisite_add_method: ['text', 'advanced'],
                             corequisite_title: 'Germany in the Nineteenth Century, 1815-1914',
                             corequisite_course: 'HIST440',
                             corequisite_phrase: 'Examines the social, economic, cultural, and political development of the major German states before 1871 and of Germany, excluding Austria, from 1871 to 1914.',

                             recommended_preparation_course: 'CHEM277',
                             recommended_preparation_title: 'Fundamentals of Analytical and Bioanalytical Chemistry Laboratory',
                             recommended_preparation_phrase: 'Quantitative analysis, inorganic analytical chemistry, and an introduction to bio-analytical instrumentation and techniques.',

                             antirequisite_course: 'HIST453',
                             antirequisite_title: 'Diplomatic History of the United States from 1914',
                             antirequisite_phrase: 'American foreign relations in the 20th-century. World War I, the Great Depression, World War II, the Cold War, the Korean War, and Vietnam. A continuation of HIST452.',

                             repeatable_for_credit_credit: rand(1..5).to_s,

                             course_that_restricts_credits_course: 'HIST454',
                             course_that_restricts_credits_title: 'Constitutional History of the United States: From Colonial Origins to 1860',
                             course_that_restricts_credits_phrase: 'The interaction of government, law, and politics in the constitutional system. The nature and purpose of constitutions and constitutionalism; the relationship between the constitution and social forces and influences, the way in which constitutional principles, rules, ideas, and institutions affect events and are in turn affected by events. The origins of American politics and constitutionalism through the constitutional convention of 1787. Major constitutional problems such as the origins of judicial review, democratization of government, slavery in the territories and political system as a whole.',

                             corequisite_add_method: 'advanced',
                             recommended_preparation_add_method: 'advanced',
                             antirequisite_add_method: 'advanced',
                             course_that_restricts_credits_add_method: 'advanced',
                             student_eligibility_add_method: 'advanced',

                             #AUTHORS & COLLABORATORS
                             author_name_method: ['advanced_name', 'advanced_username'].sample,
                             author_name_search: 'User',author_username_search: 'user1', author_display_name: 'One, User (user1)',
                             author_permission: '::random::', #['Edit, Comments, View', 'Comments, View', 'View'].sample,
                             action_request: 'FYI',

                             joint_offering_adding_data: ['adv_given_name', 'adv_course_code', 'adv_plain_text'].sample,
                             instructor_adding_method: ['adv_username', 'adv_name'].sample,
                             admin_org_adding_method: 'advanced',
                             author_name_method: ['advanced_name', 'advanced_username'].sample,



                            author_notation: :set





  #@course_proposal.create_course_proposal_required
  #@course_proposal.course_proposal_nonrequired

end


#-----
# S5
#-----
=begin
When /^I complete all fields on the course proposal with auto\-lookup$/ do
  @course_proposal  = create CmCourseProposalObject,
                           subject_code: 'MATH',
                           course_number: rand(100..999).to_s,
                           transcript_course_title:  random_alphanums(5,'test transcript'),

                           joint_offering_adding_method: ['adv_given_name', 'adv_course_code', 'adv_plain_text'].sample,
                           instructor_adding_method:   ['advanced', 'advanced_username', 'advanced_name'].sample,
                           description_rationale: random_alphanums(200),
                           proposal_rationale:    random_alphanums(200),
                           #GOVERNANCE
                           curriculum_oversight:  'CMNS-Mathematics',
                           #COURSE LOGISTICS
                           scheduling_term:      [:term_any, :term_fall, :term_spring, :term_summer],
                           final_exam_status:    [:exam_standard, :exam_alternate, :exam_none],
                           final_exam_rationale: random_alphanums(50),
                           outcome_type:       '::random::', #['Fixed', 'Multiple', 'Range'].sample,
                           outcome_value:      rand(1..3).to_s,
                           credit_value_min:   rand(1..3).to_s,
                           credit_value_max:   rand(4..8).to_s,
                           outcome_multiple:   rand(1..3).to_s,
                           outcome_multiple2:  rand(4..7).to_s,

                           activity_duration_type: '::random::', #['Day', 'Four Years', 'Half Semester', 'Hours', 'Mini-mester', 'Minutes', 'Month', 'Period', 'Quarter', 'Semester', 'Session', 'TBD', 'Term', 'Two Years', 'Week', 'Year'].sample,
                           activity_type: '::random::', #['Directed', 'Discussion', 'Experiential Learning/Other', 'Homework', 'Lab', 'Lecture', 'Tutorial', 'Web Discuss', 'Web Lecture'].sample,
                           activity_frequency: '::random::', #['per day', 'per month', 'per week'].sample,
                           activity_contacted_hours: rand(1..9).to_s,
                           activity_duration_count: rand(1..9).to_s,
                           activity_class_size: rand(1..9).to_s,

                           #ACTIVE DATES
                           start_term: 'Spring 1980',
                           pilot_course: :set,
                           end_term: 'Fall 1980',

                           #COURSE INFORMATION
                           course_listing_subject: 'FREN',
                           course_listing_number: '888',
                           joint_offering_number: 'HIST201',
                           instructor_display_name: 'SMITH, DAVID (s.davidb)',
                           instructor_first_name: 'DAVID',
                           instructor_last_name: 'SMITH',
                           instructor_username: 's.davidb',

                           instructor_adding_method:   'auto_lookup',#['auto_lookup', 'advanced', adv_username', 'adv_name'].sample,
                           joint_offering_adding_data: ['adv_given_name', 'adv_course_code', 'adv_plain_text'].sample,

                           joint_offering_name:        'Interpreting American History: From 1865 to the Present',
                           joint_offering_description: 'The United States from the end of the Civil War to the present.',
                           joint_offering_course_code: 'HIST201',

                           #GOVERNANCE
                           admin_org_adding_method:    'auto_lookup',
                           administering_organization: 'Biological Sciences',
                           campus_location:            [:location_all, :location_extended, :location_north, :location_south],
                           #BUG FOR IDENTIFIER
                           # adv_admin_org_identifier: 'ORGID-BISI',
                           adv_admin_org_name:         'Biological Sciences',
                           adv_admin_org_abbreviation: 'BISI',

                           #COURSE LOGISTICS
                           duration_type: '::random::', #['Day', 'Four Years', 'Half Semester', 'Hours', 'Mini-mester', 'Minutes', 'Month', 'Period', 'Quarter', 'Semester', 'Session', 'TBD', 'Term', 'Two Years', 'Week', 'Year'].sample,
                           duration_count: rand(1..9).to_s,
                           audit: :set, pass_fail_transcript_grade: :set,

                           #COURSE REQUISITES
                           eligibility_add_method: 'text', #['text', 'advanced'].sample,
                           rule_adv_course_title: 'American Jewish Experience', rule_adv_course_code: 'HIST106', rule_adv_course_description_snip: 'History of the Jews in America',
                           rule_course_field: '', rule_credit: rand(1..4),

                           student_eligibility_rule: 'Must have successfully completed <course>',
                           corequisiste_rule: 'Must be concurrently enrolled in <course>',
                           recommended_preparation_rule: 'Must have successfully completed <course>',
                           antirequisite_rule: 'Must not have successfully completed <course>',
                           repeatable_for_credit_rule: 'May be repeated for a maximum of <n> credits',
                           course_that_restricts_credits_rule: 'Must not have successfully completed <course>',

                           student_eligibility_course: 'HIST110',
                           student_eligibility_title: 'The Ancient World',
                           student_eligibility_phrase: 'Interpretation of select literature and art of the ancient Mediterranean world with a view to illuminating the antecedents of modern culture; religion and myth in the ancient near East; Greek philosophical, scientific, and literary invention; and the Roman tradition in politics and administration.',

                           corequisite_add_method: ['text', 'advanced'],
                           corequisite_title: 'Germany in the Nineteenth Century, 1815-1914',
                           corequisite_course: 'HIST440',
                           corequisite_phrase: 'Examines the social, economic, cultural, and political development of the major German states before 1871 and of Germany, excluding Austria, from 1871 to 1914.',

                           recommended_preparation_course: 'CHEM277',
                           recommended_preparation_title: 'Fundamentals of Analytical and Bioanalytical Chemistry Laboratory',
                           recommended_preparation_phrase: 'Quantitative analysis, inorganic analytical chemistry, and an introduction to bio-analytical instrumentation and techniques.',

                           antirequisite_course: 'HIST453',
                           antirequisite_title: 'Diplomatic History of the United States from 1914',
                           antirequisite_phrase: 'American foreign relations in the 20th-century. World War I, the Great Depression, World War II, the Cold War, the Korean War, and Vietnam. A continuation of HIST452.',

                           repeatable_for_credit_credit: rand(1..5).to_s,

                           course_that_restricts_credits_course: 'HIST454',
                           course_that_restricts_credits_title: 'Constitutional History of the United States: From Colonial Origins to 1860',
                           course_that_restricts_credits_phrase: 'The interaction of government, law, and politics in the constitutional system. The nature and purpose of constitutions and constitutionalism; the relationship between the constitution and social forces and influences, the way in which constitutional principles, rules, ideas, and institutions affect events and are in turn affected by events. The origins of American politics and constitutionalism through the constitutional convention of 1787. Major constitutional problems such as the origins of judicial review, democratization of government, slavery in the territories and political system as a whole.',

                           corequisite_add_method: 'text',
                           recommended_preparation_add_method: 'text',
                           antirequisite_add_method: 'text',
                           course_that_restricts_credits_add_method: 'text',
                           student_eligibility_add_method: 'text',

                           #AUTHORS & COLLABORATORS
                           author_name_method: 'auto_lookup',
                           author_name_search: 'User',author_username_search: 'user1', author_display_name: 'One, User (user1)',
                           author_permission: '::random::', #['Edit, Comments, View', 'Comments, View', 'View'].sample,
                           action_request: 'FYI',

                           joint_offering_adding_data: 'auto_lookup',
                           instructor_adding_method: 'auto_lookup',
                           admin_org_adding_method: 'auto_lookup',
                           author_name_method: 'auto_lookup',

                           author_notation: :set,

                           instructor_adding_method: 'auto_lookup',
                           joint_offering_adding_data: 'auto_lookup',
                           admin_org_adding_method: 'auto_lookup',
                           author_name_method: 'auto_lookup'

  #@course_proposal.create_course_proposal_required
  #@course_proposal.course_proposal_nonrequired

end
=end

