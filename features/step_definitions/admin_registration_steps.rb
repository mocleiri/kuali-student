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

    page.student_info_go  #Needed to leave the browser in a clean state
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

    page.student_info_go  #Needed to leave the browser in a clean state
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

Then /^waitlisted courses are not populated$/ do
  on(AdminRegistration).waitlisted_courses_rows.empty?.should be_true
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

    page.student_info_go  #Needed to leave the browser in a clean state
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

    page.student_info_go  #Needed to leave the browser in a clean state
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

    page.student_info_go  #Needed to leave the browser in a clean state
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

    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

When /^I select additional courses to be registered for$/ do
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "CHEM241",
                                                             :section=> "1002", :add_new_line => true,
                                                             :course_description => "Organic Chemistry II")
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL312",
                                                             :section=> "1003", :add_new_line => true,
                                                             :course_description => "Romantic to Modern British Literature")
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "PHYS739",
                                                             :section=> "1004", :add_new_line => true,
                                                             :course_description => "Seminar in Theoretical Solid State Physics")
end

Then /^I should be able to remove all the additional courses$/ do
  on AdminRegistration do |page|
    page.course_delete_btn(0).visible?.should be_true

    course_descr = @admin_reg.course_section_codes[1].course_description
    @admin_reg.course_section_codes[1].delete :index => "1", :navigate_to_page => false
    page.get_course_code_value(course_descr).nil?.should be_true
    page.get_section_value(course_descr).nil?.should be_true

    course_descr = @admin_reg.course_section_codes[2].course_description
    @admin_reg.course_section_codes[2].delete :index => "2", :navigate_to_page => false
    page.get_course_code_value(course_descr).nil?.should be_true
    page.get_section_value(course_descr).nil?.should be_true

    course_descr = @admin_reg.course_section_codes[1].course_description
    @admin_reg.course_section_codes[1].delete :index => "1", :navigate_to_page => false
    page.get_course_code_value(course_descr).nil?.should be_true
    page.get_section_value(course_descr).nil?.should be_true

    page.course_delete_btn(0).exist?.should be_false

    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

When /^I attempt to register a student for a course with default values for Credit and Registration Options$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1002", :course_default_credits => "3.0",
                                                             :course_default_reg_options => "Letter", :register =>true)
end

Then /^the default values are displayed when confirming registration$/ do
  on AdminRegistration do |page|
    page.get_course_default_credits(@admin_reg.course_section_codes[0].course_code).should match /#{@admin_reg.course_section_codes[0].course_default_credits}/
    page.get_course_default_reg_options(@admin_reg.course_section_codes[0].course_code).should match /#{@admin_reg.course_section_codes[0].course_default_reg_options}/
    page.cancel_registration

    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

When /^I attempt to register a student for a course$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "CHEM241",
                                                             :section=> "1002", :register => true)
end

Then /^the effective date should default to system date$/ do
  on AdminRegistration do |page|
    page.get_course_default_effective_date(@admin_reg.course_section_codes[0].course_code).should match /#{@admin_reg.course_section_codes[0].course_default_effective_date}/
    page.cancel_registration
    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

When /^I attempt to register a student for a cancelled course section$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1001", :register => true)
end

When /^an error message appears indicating that the section was cancelled for the selected term$/ do
  section = @admin_reg.course_section_codes[0].section
  course = @admin_reg.course_section_codes[0].course_code
  on AdminRegistration do |page|
    page.get_cancelled_section_error_message.should match /Section #{section}.*for #{course}.*was cancelled for the selected term./

    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

When /^I change the effective date of a course before confirming registration$/ do
  @admin_reg = create AdminRegistrationData, :student_id => "KS-2015", :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1003", :course_default_effective_date => tomorrow[:date_w_slashes])
  on AdminRegistration do |page|
    page.course_register
    @effective_date = @admin_reg.course_section_codes[0].course_default_effective_date
    page.set_course_default_effective_date( @admin_reg.course_section_codes[0].course_code).set "#{@effective_date}"
    page.confirm_registration
    page.dismiss_registration_result
  end
end

Then /^the registration date is displayed as a float\-over message$/ do
  on AdminRegistration do |page|
    page.registered_courses_rows[1..-1].each do |row|
        page.get_registered_course_reg_date(row).click if page.get_registered_course_reg_date(row).exists?
    end
    page.get_transaction_date_float(right_now[:date_w_slashes]).nil?.should be_false
  end
  page.student_info_go  #Needed to leave the browser in a clean state
end

When /^I register a student for a course that passed eligibility$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "CHEM241",
                                                             :section=> "1001", :register => true,
                                                             :confirm_registration => true)
end

Then /^a message indicating the course has been successfully registered appears$/ do
  on AdminRegistration do |page|
    page.get_registration_results_success.should match /Course was successfully registered./
    page.dismiss_registration_result
  end
end

Then /^the student is registered for the course$/ do
  on AdminRegistration do |page|
    page.get_registered_course("#{ @admin_reg.course_section_codes[0].course_code} (#{ @admin_reg.course_section_codes[0].section})").nil?.should be_false
    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

Then /^the student's registered courses credit total for the term should be updated$/ do
  on AdminRegistration do |page|
    @updated_credits = 0
    page.registered_courses_rows.each do |row|
      @updated_credits += page.get_registered_course_credits(row).to_i
    end
    @updated_credits.equal?(@total_credits).should be_false
  end
  page.student_info_go  #Needed to leave the browser in a clean state
end

When /^I attempt to register a student for a course that failed eligibility$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1002", :register => true,
                                                             :confirm_registration => true)
  on AdminRegistration do |page|
    page.confirm_registration_issue
    page.loading.wait_while_present
    page.dismiss_registration_result
  end
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "CHEM272",
                                                             :section=> "1001", :register => true,
                                                             :confirm_registration => true)
end

Then /^a message indicating failed eligibility for course registration appears$/ do
  on AdminRegistration do |page|
    page.get_registration_results_success.should match /Time conflict.*/
  end
end

Then /^the student is not registered for the course$/ do
  on AdminRegistration do |page|
    page.get_registered_course("#{ @admin_reg.course_section_codes[1].course_code} (#{ @admin_reg.course_section_codes[1].section})").nil?.should be_true
    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

When /^I register a student for a course$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "201301"
  on AdminRegistration do |page|
    @total_credits = 0
    page.registered_courses_rows.each do |row|
      @total_credits += page.get_registered_course_credits(row).to_i
    end
  end
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101H",
                                                             :section=> "1002", :register => true,
                                                             :confirm_registration => true)
  on AdminRegistration do |page|
    page.confirm_registration_issue
    page.loading.wait_while_present
    page.dismiss_registration_result
  end
end

When /^I open the term for registration in the Academic Calendar$/ do
  @calendar = make AcademicCalendar, :year => "2013", :name => "2012-2013 Academic Calendar"

  term = make AcademicTermObject, :parent_calendar => @calendar, :term => "Summer I", :term_type => "Summer 1"
  @calendar.terms << term

  @calendar.terms[0].edit :defer_save => true
  puts "-3"
  keydategroup = make KeyDateGroupObject, :key_date_group_type=> "Registration"
  keydates = []
  keydates << (make KeyDateObject, :key_date_type => "Registration Open", :start_date => "09/05/#{@calendar.year}",
                    :end_date => "")
  puts "-2"

  keydategroup.key_dates = keydates
  puts "-1"
  @calendar.terms[0].add_key_date_group keydategroup
end

And /^I attempt to load a Term by valid Term Id for a student with no Registered or Wait-listed courses$/ do
  @admin_reg = create AdminRegistrationData, :student_id => "KS-10296", :term_code => "201305"
end

Then /^no failed Term eligibility check or warning message is displayed$/ do
  on AdminRegistration do |page|
    page.loading.wait_while_present
    page.confirm_term_popup_section.visible?.should be_false
    page.change_term_warning_message.exists?.should be_false
    page.admin_registration_reg_for_section.visible?.should be_true
  end
end

When /^I attempt to load a Term by valid Term Id for student with Registered or Wait-listed courses$/ do
  @admin_reg = create AdminRegistrationData,:term_code => "201500"
end

Then /^the Term confirmation does not occur$/ do
  on AdminRegistration do |page|
    page.confirm_term_popup_section.visible?.should == false
  end
end

And /^a warning message confirming that the term is not open is displayed$/ do
  on AdminRegistration do |page|
    page.change_term_warning_message.visible?.should == true
    page.admin_registration_reg_for_section.visible?.should == true
  end
end

Then /^the Term confirmation does occur$/ do
  on AdminRegistration do |page|
    page.change_term_input.set "201500"
    page.change_term_go
    page.loading.wait_while_present
    page.confirm_term_popup_section.visible?.should == true
    page.confirm_term_continue

    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

When /^I decide not to continue with the selected term$/ do
  on AdminRegistration do |page|
    page.change_term_input.set "201500"
    page.change_term_go
    page.loading.wait_while_present
    page.confirm_term_popup_section.visible?.should == true
    page.confirm_term_cancel
    page.loading.wait_while_present
  end
end

Then /^only the term eligibility warning message is displayed$/ do
  on AdminRegistration do |page|
    page.get_change_term_warning_message.should match /Registration for #{page.get_change_term_info_message} is not currently open/
    page.course_addline_btn.exists?.should == false

    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

When /^I decide to continue with the selected term$/ do
  on AdminRegistration do |page|
    page.change_term_input.set "201500"
    page.change_term_go
    page.loading.wait_while_present
    page.confirm_term_popup_section.visible?.should == true
    page.confirm_term_continue
  end
end

Then /^a warning message along with the Registered and Wait-listed courses are displayed$/ do
  on AdminRegistration do |page|
    page.change_term_warning_message.visible?.should == true
    page.admin_registration_reg_for_section.visible?.should == true

    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

When /^I attempt to register a student for courses with more credits than the allowed maximum$/ do
  @admin_reg = create AdminRegistrationData, :student_id => "KS-2011", :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1002", :register => true,
                                                             :confirm_registration => true,
                                                             :dismiss_registration_result => true)
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1003", :register => true,
                                                             :confirm_registration => true,
                                                             :dismiss_registration_result => true)
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1004", :register => true,
                                                             :confirm_registration => true,
                                                             :dismiss_registration_result => true)
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1005", :register => true,
                                                             :confirm_registration => true,
                                                             :dismiss_registration_result => true)
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1006", :register => true,
                                                             :confirm_registration => true,
                                                             :dismiss_registration_result => true)
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1020", :register => true,
                                                             :confirm_registration => true,
                                                             :dismiss_registration_result => true)
end

Then /^multiple failed eligibility messages appear$/ do
  on AdminRegistration do |page|
    result_success = page.get_registration_results_success
    result_success.should match /Time conflict/
    result_success.should match /Reached maximum credit limit./
    page.deny_registration_issue
  end
end

When /^I deny the course for registration$/ do
  on AdminRegistration do |page|
    page.deny_registration_issue
  end
end

When /^I allow the course for registration$/ do
  on AdminRegistration do |page|
    page.confirm_registration_issue
    page.loading.wait_while_present
    page.dismiss_registration_result
  end
end

And /^I attempt to register the student for a course with a time conflict$/ do
  @admin_reg = create AdminRegistrationData, :student_id => "KS-2011", :term_code=> "201301"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                               :section=> "1021", :register => true,
                                                               :confirm_registration => true)
  on AdminRegistration do |page|
    page.confirm_registration_issue
    page.loading.wait_while_present
    page.dismiss_registration_result
  end
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "CHEM272",
                                                             :section=> "1001", :register => true,
                                                             :confirm_registration => true)

end

When /^I attempt to edit a course with default values for Credit and Registration Options$/ do
  @admin_reg = create AdminRegistrationData, :student_id => "KS-11898", :term_code => "201208"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL101",
                                                             :section=> "1002", :course_default_credits => "3.0",
                                                             :course_default_reg_options => "Letter", :register => true,
                                                             :confirm_registration => true)
  on AdminRegistration do |page|
    page.confirm_registration_issue
    page.loading.wait_while_present
    page.dismiss_registration_result
  end

  @admin_reg.course_section_codes[0].edit_course
end

Then /^the default values are displayed on edit course dialog$/ do
  on AdminRegistration do |page|
    page.get_edited_reg_course_default_credits(@admin_reg.course_section_codes[0].course_code).should match /#{@admin_reg.course_section_codes[0].course_default_credits}/
    page.get_edited_reg_course_default_reg_options(@admin_reg.course_section_codes[0].course_code).should match /#{@admin_reg.course_section_codes[0].course_default_reg_options}/
    page.cancel_edited_course
    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

When /^I attempt to edit a registered course$/ do
  @admin_reg = create AdminRegistrationData, :student_id => "KS-11899", :term_code=> "201208"
  @admin_reg.add_course_section :course_section_obj => (make ARCourseSectionObject, :course_code=> "ENGL304",
                                                             :section=> "1001", :register => true,
                                                             :confirm_registration => true ,
                                                             :course_default_effective_date => tomorrow[:date_w_slashes])
  on AdminRegistration do |page|
    page.confirm_registration_issue
    page.loading.wait_while_present
    page.dismiss_registration_result
  end

  @admin_reg.course_section_codes[0].edit_course
end

Then /^I save the edited course with no effective date$/ do
  on AdminRegistration do |page|
    page.set_edited_reg_course_effective_date( @admin_reg.course_section_codes[0].course_code).set nil
    page.save_edited_course
  end
end

Then /^a message appears indicating that the effective date is required$/ do
  on AdminRegistration do |page|
    page.loading.wait_while_present
    page.edit_course_dialog_error_msg.should match /Effective date is required/
    page.cancel_edited_course

    page.student_info_go  #Needed to leave the browser in a clean state
  end
end

Then /^I save the changes made to Registration Options and Effective Date$/ do
  on AdminRegistration do |page|
    page.set_edited_reg_course_reg_options( @admin_reg.course_section_codes[0].course_code).select "Audit"
    page.set_edited_reg_course_effective_date( @admin_reg.course_section_codes[0].course_code).set in_a_week[:date_w_slashes]
    page.save_edited_course
  end
end

Then /^a message appears indicating that the course has been updated successfully$/ do
  on AdminRegistration do |page|
    page.loading.wait_while_present
    page.confirm_registration_issue
    page.loading.wait_while_present
    page.get_registration_results_success.should match /Course was successfully updated/

    page.student_info_go  #Needed to leave the browser in a clean state
  end
end