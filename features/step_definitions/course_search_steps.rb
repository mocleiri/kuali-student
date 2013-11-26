When /^I search for a course$/ do
  @course_offering = make CourseOffering
  @course_offering.course_search
end

When /^I enter the course in the search field$/ do
  @course_offering = make CourseOffering
  @course_offering.set_search_entry

end


Then /^I clear the search entry$/ do
@course_offering.clear_search
end

Then /^the search entry should be cleared successfully$/ do
  on CourseSearch do |page|
       page.search_for_course == ""
    end

end

Then /^the course (.*) appear in the search results$/ do |test_condition|
  on CourseSearch do |page|
    if test_condition == "should"
      page.results_list.should include @course_offering.course_code
    else
      begin
        page.results_list.should_not include @course_offering.course_code
      rescue Watir::Exception::UnknownObjectException
        # Implication here is that there were no search results at all.
      end
    end
  end
end


And /^the search results list should be cleared successfully$/ do
  on CourseSearch do |page|
    page.results_list == ""
  end
end