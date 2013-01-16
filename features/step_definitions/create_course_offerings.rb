When /^I designate a valid term and Catalog Course Code$/ do
  go_to_create_course_offerings
  on CreateCourseOffering do  |page|
    page.target_term.set "20122"
    page.catalogue_course_code.set "CHEM142"
    page.show
  end
end

And /^I create a Course Offering with selected Delivery Formats$/ do
  on CreateCourseOffering do |page|
    page.suffix.set "G"
    page.select_delivery_format("Lecture/Quiz")
    page.select_grade_roster_level("Quiz")
    page.select_final_exam_driver("Quiz")
    page.delivery_format_add
    page.create_offering
  end
end

Then /^the new Course Offering should contain only the selected delivery formats$/ do
  pending # this step has not yet been implemented
end

And /^I configure a course offering copy from an existing offering to exclude instructor information$/ do
  on CreateCourseOffering do |page|
    page.create_from_existing_offering_tab
    page.configure_course_offering_copy_toggle
    page.select_exclude_instructor_checkbox
  end
end

And /^I press Copy$/ do
  on CreateCourseOffering do |page|
    page.create_from_existing_offering_copy_submit
    @newly_created_co = /Course offering (.+) has been successfully created/.match(page.growl_message.text)[1]
    page.create_offering
  end
end

Then /^the new Course Offering should be displayed in the list of available offerings\.$/ do
  @course_offering = make CourseOffering, :course=>@newly_created_co
  @course_offering.manage
  on ManageCourseOfferings do |page|
    page.error_message_course_not_found.should_not be_present
  end
  @course_offering.delete_co :should_delete_from_subj_code_view=>false, :should_confirm_delete=>true
end