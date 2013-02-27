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

Then /^I do not have access to create the course offering$/ do
   on CreateCourseOffering do |page|
     page.auth_error.present?.should == true
   end
end

When /^I navigate to create course offerings and designate a valid term and Catalog Course Code not in my department$/ do
  @course_offering = make CourseOffering, :term=> "201312", :course => "CHEM132"
  @course_offering.create_by_search
end

When /^I navigate to create course offerings and designate a valid term and Catalog Course Code in my department$/ do
  @course_offering = make CourseOffering, :term=> "201312", :course => "ENGL310"
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