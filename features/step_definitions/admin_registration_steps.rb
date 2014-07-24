When /^I attempt to load a student by valid student Id$/ do
  @admin_reg = create AdminRegistrationData
end

Then /^student basic information and change term section is displayed$/ do
  on AdminRegistration do |page|
    page.student_info_msg.text.should =~ /BOWEN, EILEEN \(#{@admin_reg.student_id.upcase}\)/
    page.registration_change_term_section.visible?.should == true
  end
end

When /^I attempt to load a student by invalid student Id$/ do
  @admin_reg = create AdminRegistrationData, :student_id=> "student1"
end

Then /^a validation error message displayed stating "([^"]+)"$/ do |exp_msg|
  on(AdminRegistration).get_student_error_message.should match /#{exp_msg}/
end

When /^I attempt to load a Term by valid term Id for student with no registered courses$/ do
  @admin_reg = create AdminRegistrationData, :student_id => "KS-10296", :term_code => "201208"
end

When /^I attempt to load a Term by valid term Id$/ do
  @admin_reg = create AdminRegistrationData, :term_code => "201208", :term_description => "Fall 2012"
end

Then /^term description is displayed$/ do
  on(AdminRegistration).get_change_term_info_message.should == @admin_reg.term_description
end

When /^I attempt to load a Term by invalid term Id$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "558899"
end

Then /^error message is displayed stating "(.*?)"$/ do |exp_msg|
  on(AdminRegistration).get_term_error_message.should match /#{exp_msg}/
end

When /^I attempt to load a Term without entering a term Id$/  do
  @admin_reg = create AdminRegistrationData, :term_code => " "
end

Then /^a required error message is displayed stating "(.*?)"$/ do |exp_msg|
  on AdminRegistration do |page|
    page.get_term_error_message.should match /#{exp_msg}/
    #temporary work around to leave the browser in a clean state
    page.student_info_go
  end
end

And /^registered courses are populated$/ do
  on(AdminRegistration).registered_courses_rows.empty?.should be_false
end

And /^the total number of credits for registered courses are displayed$/ do
  on AdminRegistration do |page|
    credits = 0
    page.registered_courses_rows.each do |row|
      credits += page.get_registered_course_credits(row).to_i
    end
    page.registered_courses_header.should match /#{credits.to_s}/
    #temporary work around to leave the browser in a clean state
    page.student_info_go
  end
end

Then /^registered courses are not populated$/ do
  on(AdminRegistration).registered_courses_rows.empty?.should be_true
end

Then /^the default sort order for registered courses should be on course code$/ do
  on(AdminRegistration).get_registered_course_code_sort.should match /Course Code \(Section\)/
end

And /^I attempt to load a Term by valid term Id for student with waitlisted courses$/ do
  @admin_reg = create AdminRegistrationData, :student_id=> "KS-7185", :term_code=> "201208"
end

And /^waitlisted courses are populated$/ do
  on(AdminRegistration).waitlisted_courses_rows.empty?.should be_false
end

And /^the total number of credits for waitlisted courses are displayed$/ do
  on AdminRegistration do |page|
    credits = 0
    page.waitlisted_courses_rows.each do |row|
      credits += page.get_waitlisted_course_credits(row).to_i
    end
    page.waitlisted_courses_header.should match /#{credits.to_s}/
  end
end

Then /^waitlisted courses table is not populated with courses$/ do
  on(AdminRegistration).waitlisted_courses_row.should == nil
end

Then /^the default sort order for waitlisted courses should be on course code$/ do
  on(AdminRegistration).get_waitlisted_course_code_sort.should match /Course Code \(Section\)/
end

When /^I enter a valid course code for term$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject,
                                                             :course_description=> "The Major Works of Shakespeare")
end

When /^I enter a valid section for course code$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject)
end

Then /^the course description is displayed$/ do
  on AdminRegistration do |page|
    page.course_addline_btn.focus
    page.get_course_description_message.should == @admin_reg.course_section_codes[0].course_description
    #temporary work around to leave the browser in a clean state
    page.student_info_go
  end
end

When /^I enter an invalid course code for term$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL299")
end

Then /^the error message for course code is displayed stating "([^"]*)"$/ do |exp_msg|
  on AdminRegistration do |page|
    page.course_register
    page.loading.wait_while_present
    page.reg_for_error_message.text.should match /#{exp_msg}/
    #temporary work around to leave the browser in a clean state
    page.student_info_go
  end
end

When /^I enter an invalid course code$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENG299L")
end

When /^I enter an invalid section$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :section=> "100d")
end

Then /^the section code should appear on the confirm registration dialog$/ do
  on AdminRegistration do |page|
    page.course_register
    page.loading.wait_while_present
    page.get_confirm_registration_row("#{@admin_reg.course_section_codes[0].course_code} (#{@admin_reg.course_section_codes[0].section})").nil?.should be_false
    page.cancel_registration
    #temporary work around to leave the browser in a clean state
    page.student_info_go
  end
end

When /^I select the course that a student will be registered for$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject)
end

Then /^I should be able to select additional courses for the student$/ do
  on AdminRegistration do |page|
    @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "CHEM241",
                                                               :section=> "1002", :add_new_line => true)
    page.get_last_course_code_value.should match /#{@admin_reg.course_section_codes[1].course_code}/i
    page.get_last_section_value.should match /#{@admin_reg.course_section_codes[1].section}/i

    @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL312",
                                                               :section=> "1003", :add_new_line => true)
    page.get_last_course_code_value.should match /#{@admin_reg.course_section_codes[2].course_code}/i
    page.get_last_section_value.should match /#{@admin_reg.course_section_codes[2].section}/i

    @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "PHYS739",
                                                               :section=> "1004", :add_new_line => true)
    page.get_last_course_code_value.should match /#{@admin_reg.course_section_codes[3].course_code}/i
    page.get_last_section_value.should match /#{@admin_reg.course_section_codes[3].section}/i
  end
end