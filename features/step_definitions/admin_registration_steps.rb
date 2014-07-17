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
