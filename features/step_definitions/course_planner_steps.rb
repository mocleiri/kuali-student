When /^I add a fixed credit course with notes to current term$/ do

  #Fixed Credit Course BSCI430
  #Current Term Fall 2013

  @course_offering = make CoursePlanner, :course_code => "BSCI430"
  @course_offering.add_course_to_current_term

end



When /^I add a variable credit course with notes to a future term$/ do
  pending # express the regexp above with the code you wish you had
  #Var Credit Course ENGL388
  #Future Term Spring 2015
  @course_offering = make CoursePlanner, :course_code => "ENGL388"
  @course_offering.add_course_to_future_term
end



And /^I view the details of the added course from edit plan item$/ do

end


Then /^fixed credit and note details are displayed under the current term$/ do
pending # express the regexp above with the code you wish you had
  #Fixed Credit Course BSCI430
  #Current Term Fall 2013
end

Then /^the course should be added to the current term$/ do

  on CoursePlannerPage do |page|
    page.growl_text.should include "Course #{@course_offering.course_code} added to plan for Fall 2013"
    end


end


Then /^the course should be added to the future term$/ do
pending # express the regexp above with the code you wish you had
end


Then /^variable credit and note details are displayed under the future term$/ do
pending # express the regexp above with the code you wish you had
  #Var Credit Course ENGL388
  #Future Term Spring 2015
end