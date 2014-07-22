And(/^I manage comments for an activity offering$/) do
  @course_offering = create CourseOffering, :create_by_copy=>(make CourseOffering, :course=>"PHYS272")
  @course_offering.initialize_with_actual_values
end

When(/^I add a new Comment$/) do
  @course_offering.get_ao_obj_by_code('A').add_admin_comment :comment_obj => (make AdminCommentObject)

end