When /^I search for a course$/ do
  @course_offering = make CourseOffering
  @course_offering.navigate_to_course_search_home
  @course_offering.set_search_entry
  @course_offering.course_search
end

Then /^I clear the search entry$/ do
@course_offering.clear_search_entry
 end

Then /^the search entry should be cleared successfully$/ do
     @course_offering.verify_search_entry_clear
end



When /^I enter the course in the search field$/ do
@course_offering = make CourseOffering
@course_offering.navigate_to_course_search_home
@course_offering.set_search_entry
end


Then /^the course (.*) appear in the search results$/ do |test_condition|
  on CourseSearch do |page|
    if test_condition == "should"
      page.results_list.should include @course_offering.course_code
    else
      begin
        page.results_list.should_not include arg1
      rescue Watir::Exception::UnknownObjectException
        # Implication here is that there were no search results at all.
      end
    end
  end
end


And /^the search results list should be cleared successfully$/ do
  @course_offering.verify_search_result_clear
end