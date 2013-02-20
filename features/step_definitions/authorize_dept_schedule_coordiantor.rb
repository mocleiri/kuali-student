When /^I navigate to the edit activity offering$/ do
  @course_offering = create CourseOffering, :create_by_copy=>(make CourseOffering, :course=>"ENGL310")
end
Then /^I do not have access to add or edit seat pools$/ do
  pending
end