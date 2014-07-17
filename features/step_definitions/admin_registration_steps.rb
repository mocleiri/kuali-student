When /^I attempt to load a student by valid student Id$/ do
  @admin_reg = create AdminRegistrationData
end

Then(/^student information and change term section is displayed$/) do
  on AdminRegistration do |page|
    page.student_info_msg.text.should =~ /BOWEN, EILEEN \(#{@admin_reg.student_id.upcase}\)/
    page.registration_change_term_section.visible?.should == true
  end
end

When /^I attempt to load a student by invalid student Id$/ do
  @admin_reg = create AdminRegistrationData, :student_id=> "student1"
end

Then /^a validation error message displayed stating "([^"]+)"$/ do |exp_msg|
  on AdminRegistration do |page|
    page.get_student_error_message.should match /#{exp_msg}/
  end
end

When /^I search for a term by valid term code for student with no registered courses$/ do
  @admin_reg = create AdminRegistrationData, :student_id => "KS-10296", :term_code => "201208"
end

When /^I search for a term by valid term code$/ do
  @admin_reg = create AdminRegistrationData, :term_code => "201208"
end

Then /^term description is displayed stating "([^"]+)"$/ do |exp_msg|
  on AdminRegistration do |page|
    page.get_change_term_info_message.should =~ /#{exp_msg}/
  end
end

When /^I search for a term by invalid term code$/ do
  @admin_reg = create AdminRegistrationData, :term_code=> "558899"
end

Then /^error message is displayed stating "(.*?)"$/ do |exp_msg|
  on AdminRegistration do |page|
    page.get_term_error_message.should match /#{exp_msg}/
  end
end

When /^I search for a term without entering a term code$/  do
  @admin_reg = create AdminRegistrationData, :term_code => " "
end

Then /^a required error message is displayed stating "(.*?)"$/ do |exp_msg|
  on AdminRegistration do |page|
    page.get_term_error_message.should match /#{exp_msg}/
  end
end

And /^registered courses table is populated with courses$/ do
  on AdminRegistration do |page|
    page.registered_courses_row.empty?.should be_false
  end
end

And /^table header should contain total number of credits for registered courses$/ do
  on AdminRegistration do |page|
    credits = 0
    page.registered_courses_row.each do |row|
      credits += page.get_registered_course_credits(row).to_i
    end
    page.registered_courses_header.should match /#{credits.to_s}/
  end
end

Then(/^registered courses table is not populated with courses$/) do
  on AdminRegistration do |page|
    page.registered_courses_row.should == nil
  end
end

Then(/^the default sort order for registered courses should be on course code$/) do
  on AdminRegistration do |page|
    page.get_registered_course_code_sort.should match /Course Code \(Section\)/
  end
end

And /^I search for a term by valid term code for student with waitlisted courses$/ do
    @admin_reg = create AdminRegistrationData, :student_id=> "KS-7185", :term_code=> "201208"
end

And /^waitlisted courses table is populated with courses$/ do
  on AdminRegistration do |page|
    page.waitlisted_courses_row.empty?.should be_false
  end
end

And /^table header should contain total number of credits for waitlisted courses$/ do
  on AdminRegistration do |page|
    credits = 0
    page.waitlisted_courses_row.each do |row|
      credits += page.get_waitlisted_course_credits(row).to_i
    end
    page.waitlisted_courses_header.should match /#{credits.to_s}/
  end
end

Then(/^waitlisted courses table is not populated with courses$/) do
  on AdminRegistration do |page|
    page.waitlisted_courses_row.should == nil
  end
end

Then(/^the default sort order for waitlisted courses should be on course code$/) do
  on AdminRegistration do |page|
    page.get_waitlisted_course_code_sort.should match /Course Code \(Section\)/
  end
end



