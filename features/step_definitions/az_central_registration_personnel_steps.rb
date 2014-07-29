When(/^I navigate to Admin Registration$/) do
  @admin_reg = make AdminRegistrationData
  @admin_reg.search
end

Then /^I have access to select a student$/ do
  on(AdminRegistration).student_info_go_btn.present?.should be_true
end