And /^term contains COs in Draft state$/ do
  @course_offering = make CourseOffering, :term=>"201805", :course=>"ENGL206"
  @course_offering.search_by_subjectcode

  on ManageCourseOfferings do |page|
    page.ao_status("ENGL206", "Draft").should == true
    page.ao_status("ENGL222", "Draft").should == true
    page.ao_status("ENGL301", "Draft").should == true
  end
end

When /^I select a course offering containing at least one activity in Draft state$/ do
  @course_offering.search_by_coursecode
  on ManageCourseOfferings do |page|
    page.ao_status("A", "Draft").should == true
  end
 end

And /^I click the Approve toolbar button$/ do
  @course_offering.search_by_subjectcode
  on(ManageCourseOfferings).approve_co :code_list => [@course_offering.course]
end

And /^I cancel the action$/ do
  on(ManageCourseOfferings).approve_co_cancel_action
end

Then /^the list of courses remains unchanged$/ do
  on ManageCourseOfferings do |page|
    page.ao_status("ENGL206", "Draft").should == true
  end
end

And /^Draft activity offerings remain in Draft state$/ do
  @course_offering.search_by_coursecode
  on ManageCourseOfferings do |page|
    page.ao_status("A", "Draft").should == true
  end
end

And /^I confirm the operation$/ do
  on(ManageCourseOfferings).approve_co_confirm_action
end

Then /^the course offering is in Planned state$/ do
  on ManageCourseOfferings do |page|
    page.ao_status("ENGL206", "Planned").should == true
  end
 end

And /^its activity offerings are in Approved state$/ do
  @course_offering.search_by_coursecode
  on ManageCourseOfferings do |page|
    page.check_all_ao_status("Approved").should == true
  end
 end

When /^I select multiple course offerings each containing at least one activity in Draft state$/ do
pending # express the regexp above with the code you wish you had
end

Then /^the selected course offerings are in Planned state$/ do
pending # express the regexp above with the code you wish you had
end

And /^their activity offerings are in Approved state$/ do
pending # express the regexp above with the code you wish you had
end