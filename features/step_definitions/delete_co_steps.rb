When /^I cancel the deletion of a Course Offering in Course Offering Code view$/ do
  @course_offering = make CourseOffering
  @course_offering.create_co_copy
  on ManageCourseOfferings do |page|
    page.target_row(@course_offering.course).link(text: "Manage").click
    page.delete_offering
  end
  on DeleteCourseOffering do |page|
    page.cancel_delete
  end
end

When /^I cancel the deletion of a Course Offering in Subject Code view$/ do
  @course_offering = make CourseOffering
  @course_offering.create_co_copy
  @course_offering.search_by_subjectcode
  on ManageCourseOfferings do |page|
    page.target_row(@course_offering.course).link(text: "Delete").click
  end
  on DeleteCourseOffering do |page|
    page.cancel_delete
  end
end

Then /^the Course Offering is not deleted$/ do

  #ensure CO still exists
  go_to_manage_course_offerings
  on ManageCourseOfferings do |page|
    page.term.set @course_offering.term
    page.input_code.set @course_offering.course
    page.show
    page.error_message_course_not_found.should_not be_present
  end

  #cleanup
  @course_offering.delete_co :should_confirm_delete => true
end

When /^I delete a Course Offering with Draft Activity Offerings in Course Offering Code view$/ do
  @course_offering = make CourseOffering
  @course_offering.create_co_copy
  on ManageCourseOfferings do |page|
    page.target_row(@course_offering.course).link(text: "Manage").click
    page.ao_status("A", "Draft").should == true
    page.delete_offering
  end
  on DeleteCourseOffering do |page|
    page.confirm_delete
  end
end

When /^I delete a Course Offering with Draft Activity Offerings in Subject Code view$/ do
  @course_offering = make CourseOffering
  @course_offering.create_co_copy
  @course_offering.search_by_subjectcode
  on ManageCourseOfferings do |page|
    page.target_row(@course_offering.course).link(text: "Delete").click
  end
  on DeleteCourseOffering do |page|
    page.confirm_delete
  end
end

Then /^the deleted course offering does not appear on the list of available Course Offerings$/ do
  #ensure CO does not exist
  go_to_manage_course_offerings
  on ManageCourseOfferings do |page|
    page.term.set @course_offering.term
    page.input_code.set @course_offering.course
    page.show
    page.error_message_course_not_found.should be_present
  end
end