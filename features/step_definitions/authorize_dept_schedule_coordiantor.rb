When /^I navigate to the edit activity offering for a course offering in my department in "final edits"$/ do
  @course_offering = make CourseOffering, :course=>"ENGL362" , :term=>"201612"
  @course_offering.search_by_coursecode
  on ManageCourseOfferings do |page|
    page.edit("A")
  end
end


When /^I navigate to the edit activity offering for a course offering for a term in "final edits"$/ do
 @course_offering = make CourseOffering, :course=>"CHEM611" , :term=>"201612"
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

When /^I navigate to manage course offerings for a course offering not in my department$/ do
  @course_offering = make CourseOffering, :course=>"CHEM611", :term=>"201512"
  @course_offering.search_by_coursecode
end

Then /^I do not have access to edit the course offering$/ do
  on ManageCourseOfferings do |page|
    page.edit_offering_element.present?.should == false
  end
end

When /^I navigate to manage course offerings for a course in my department $/ do
  @course_offering = make CourseOffering, :course=>"ENGL346", :term=>"201612"
  @course_offering.search_by_coursecode
end