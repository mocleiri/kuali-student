When /^I manage a single course offering$/ do
  @course_offering = make CourseOffering, :term=>"201605", :course=>"ENGL206"
  @course_offering.search_by_coursecode
end

And /^I prepare Activity Offering data$/ do
  on ManageCourseOfferings do |page|
    @total_number=page.codes_list.count
    format = page.format.options[1].text
    page.add_ao format, 1
  end
end

And /^I click the Add Activity Offering button$/ do
  on(ManageCourseOfferings).add_activity
end

Then /^the Activity Offering should be added to the list$/ do
  on ManageCourseOfferings do |page|
    new_total = page.codes_list.count
    new_total.should == @total_number + 1
  end
end