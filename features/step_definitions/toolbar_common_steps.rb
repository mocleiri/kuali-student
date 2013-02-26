Given /^I manage a single course offering$/ do
  @course_offering = make CourseOffering, :term=>"201605", :course=>"ENGL206"
  @course_offering.search_by_coursecode
end

When /^I click the "(.*?)" toolbar button$/ do |button|
  on(ManageCourseOfferings).click_toolbar_button(button)
end
