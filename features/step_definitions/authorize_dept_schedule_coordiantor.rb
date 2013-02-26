When /^I navigate to the edit activity offering$/ do
  @course_offering = make CourseOffering, :course=>"ENGL346"
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

When /^I navigate to the edit activity offering logistics schedule page for a course in my department$/ do
  @course_offering = make CourseOffering, :course=>"ENGL362", :term=>"201612"
  @course_offering.search_by_coursecode
  on ManageCourseOfferings do |page|
    page.edit("A")
  end
  on ActivityOfferingMaintenance do |page|
    page.revise_actual_delivery_logistics
  end
end

When /^I navigate to manage course offerings for a course not in my department$/ do
  @course_offering = make CourseOffering, :course=>"CHEM611", :term=>"201512"
  @course_offering.search_by_coursecode
end

Then /^I do not have access to edit the course offering$/ do
  on ManageCourseOfferings do |page|
    page.edit_offering_element.present? == false
  end
end