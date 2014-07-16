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