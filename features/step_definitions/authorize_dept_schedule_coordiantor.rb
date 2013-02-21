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