When /^I cancel the deletion of a Course Offering in Course Offering Code view$/ do
  @course_offering = make CourseOffering
  @course_offering.course = @course_offering.create_co_copy
  @course_offering.delete_co :should_confirm_delete => false
end

When /^I cancel the deletion of a Course Offering in Subject Code view$/ do

  #create data
  @course_offering = make CourseOffering, :course=>"ENGL103" #depend on targeting 'ENGL' later in test
  @newly_created_course = @course_offering.create_co_copy

  #click 'Delete' in the Subject Code View
  on ManageCourseOfferings do |page|
    page.term.set @course_offering.term
    page.input_code.set "ENGL"
    page.show
    page.target_row(@newly_created_course).link(text: "Delete").click
  end

  puts 'getting ready to click the cancel button'
  sleep 10
  on DeleteCourseOffering do |page|
    @course_offering.delete_co :should_confirm_delete => false
  end

  #preparatory cleanup
  @course_offering.course = @newly_created_course
end

Then /^the Course Offering is not deleted$/ do

  #ensure CO still exists
  puts 'doing cleanup'
  go_to_manage_course_offerings
  on ManageCourseOfferings do |page|
    page.term.set @course_offering.term
    page.input_code.set @course_offering.course
    page.show
    page.error_message_course_not_found.should_not be_present
  end

  #cleanup
  @course_offering.delete_co :should_confirm_delete => true

  puts 'done'
  sleep 60
end