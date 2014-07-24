And(/^I add a comment to an activity offering$/) do
  @course_offering = create CourseOffering, :create_by_copy=>(make CourseOffering, :course=>"PHYS272")
  @course_offering.initialize_with_actual_values
  @course_offering.get_ao_obj_by_code('A').add_admin_comment :comment_obj => (make AdminCommentObject)
end

And(/^I add a comment to a course offering$/) do
  @course_offering = create CourseOffering, :create_by_copy=>(make CourseOffering, :course=>"CHEM481")
  @course_offering.add_admin_comment :comment_obj => (make AdminCommentObject)
end

Then(/^the activity offering comment.*can be viewed successfully$/) do
  @course_offering.get_ao_obj_by_code('A').manage_comments
  comment = @course_offering.get_ao_obj_by_code('A').admin_comments_list[0]
  on AdminComments do |page|
    comment_div = page.comment_by_text(comment.text)
    page.comment_text(comment_div).should == comment.text
    page.comment_created_by(comment_div).should == comment.creator
    page.comment_created_date(comment_div).should == comment.created_date
    page.header_text_comments_count.should == @course_offering.get_ao_obj_by_code('A').admin_comments_list.length
    page.close
  end
end

And(/^I manage comments for an activity offering with existing comments$/) do
  @course_offering = create CourseOffering, :create_by_copy=>(make CourseOffering, :course=>"PHYS272")
  #@course_offering = make CourseOffering, :course=>"PHYS272Q"
  @course_offering.initialize_with_actual_values
  @course_offering.get_ao_obj_by_code('A').add_admin_comment :comment_obj => (make AdminCommentObject)
end

When(/^I delete an existing comment$/) do
  @course_offering.get_ao_obj_by_code('A').admin_comments_list[0].delete
end

Then(/^the comment is successfully deleted$/) do
  @course_offering.get_ao_obj_by_code('A').manage_comments
  on AdminComments do |page|
    page.comments_list.length.should == 0
    page.header_text_comments_count.should == 0
    page.close
  end
end

When(/^I select the delete option for an existing comment but choose not to confirm the deletion$/) do
  @course_offering.get_ao_obj_by_code('A').admin_comments_list[0].delete :confirm_delete => false
end

When(/^I edit an existing activity offering comment and save$/) do
  @course_offering.get_ao_obj_by_code('A').admin_comments_list[0].edit :text => "edited text #{random_alphanums(3)}"
end

Then(/^the course offering comment can be viewed successfully$/) do
  pending
end

Then(/^the comment is updated successfully$/) do
  @course_offering.get_ao_obj_by_code('A').manage_comments
  comment = @course_offering.get_ao_obj_by_code('A').admin_comments_list[0]
  on AdminComments do |page|
    comment_div = page.comment_by_text(comment.text)
    page.comment_text(comment_div).should == comment.text
    page.comment_edited_by(comment_div).should == comment.last_editor
    page.comment_edited_date(comment_div).should == comment.edited_date
    page.header_text_comments_count.should == @course_offering.get_ao_obj_by_code('A').admin_comments_list.length
    page.close
  end
end