Then /^I am given the option to add myself to a waitlist for the course$/ do
  on RegistrationCart do |page|
    page.add_to_waitlist_button(@reg_request.course_code,@reg_request.reg_group_code).wait_until_present
  end
end

When /^I add myself to a waitlist for the course$/ do
  on RegistrationCart do |page|
    page.add_to_waitlist(@reg_request.course_code,@reg_request.reg_group_code)
  end
end

Then /^there is a message indicating that I have been added to the waitlist$/  do
  on RegistrationCart do |page|
    page.waitlist_status(@reg_request.course_code,@reg_request.reg_group_code).should match /added to waitlist/i
  end
end

Then /^there is a message indicating that registration failed$/  do
  on RegistrationCart do |page|
    page.course_code(@reg_request.course_code,@reg_request.reg_group_code).text.should match /failed/i
  end
end

And /^there is a message indicating that no waitlist is offered$/  do
  on RegistrationCart do |page|
    page_status = page.result_status(@reg_request.course_code,@reg_request.reg_group_code).downcase
    page_status.should match /waitlist not offered/i
  end
end

And /^I can verify I am on the waitlist$/  do
  #check that course appears as a waitlisted course
  on StudentSchedule do |page|
    page.waitlisted_course_code(@reg_request.course_code,@reg_request.reg_group_code).wait_until_present
  end
end

Given /^I register for an? full (\w+) course offering that (has|does not have) a waitlist$/ do |subj,waitlist|
  #course has 1 remaining seat, so first fill it as admin, then try to register as student
  steps %{
    When I add an #{subj} course offering to my registration cart
    And I register for the course
    And I view my schedule
    Then the course is present in my schedule
    When I log out
    And I am logged in as a Student
    And I add an #{subj} course offering to my registration cart
    And I register for the course
  }
end

