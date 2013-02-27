When /^I attempt to edit an activity offering for a course offering in my department$/ do
  @course_offering = make CourseOffering, :course=>"ENGL362" , :term=>@term_for_test
  @course_offering.search_by_coursecode
  on ManageCourseOfferings do |page|
    page.edit("A")
  end
end

Then /^I do not have access to add or edit seat pools$/ do
  on ActivityOfferingMaintenance do |page|
    page.add_pool_element.present?.should == false
  end
end

When /^I attempt to edit a course not in my department$/ do
  @course_offering = make CourseOffering, :course=>"CHEM611", :term=>@term_for_test
  @course_offering.search_by_coursecode
end

Then /^I do not have access to edit the course offering$/ do
  on ManageCourseOfferings do |page|
    page.edit_offering_element.present?.should == false
  end
end

When /^I manage course offerings for a course in my department$/ do
  @course_offering = make CourseOffering, :course=>"ENGL346", :term=>"201612"
  @course_offering.search_by_coursecode
end


#When /^I manage course offerings for a subject code in my department$/ do
#  @term_for_test = Rollover::OPEN_SOC_TERM unless @term_for_test != nil
#  @course_offering = make CourseOffering, :course=>"ENGL346", :term=>@term_for_test
#  @course_offering.search_by_coursecode
#end


Then /^I can view course offering details$/ do
  on ManageCourseOfferingList do |page|
    page.copy_link(@course_offering.course).present?.should == false
  end
end

Then /^I can add new course offerings$/ do

end

Then /^I can approve course offerings for scheduling$/ do

end

Then /^I can delete course offerings$/ do

end

Then /^I can edit course offerings$/ do

end

Then /^I can copy course offerings$/ do

end

Then /^I do not have access to create the course offering$/ do
   on CreateCourseOffering do |page|
     page.auth_error.present?.should == true
   end
end

When /^I attempt to create a course not in my department$/ do
  @course_offering = make CourseOffering, :term=> @term_for_test, :course => "CHEM132"
  @course_offering.create_by_search
end

When /^I attempt to create a course in my department$/ do
  @course_offering = make CourseOffering, :term=> @term_for_test, :course => "ENGL310"
  @course_offering.create_by_search
end
Then /^I have access to create the course offering from catalog$/ do
  on CreateCourseOffering do |page|
   page.suffix.present?.should == true
  end
end
When /^I have access to create the course from an existing offering$/ do
  on CreateCourseOffering do |page|
    page.create_from_existing_offering_tab
    page.configure_course_offering_copy_element.present?.should == true
  end

end