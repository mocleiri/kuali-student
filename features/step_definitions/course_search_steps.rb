=begin
Given /^I am logged in as a Student$/ do
  pending # express the regexp above with the code you wish you had
end
=end

And /^I am on the KSAP Course Search Page$/ do
  on KSFunctionalHome do |page|
    page.course_search_home
  end
end

When /^I search for a course$/ do
  on CourseSearch do |page|
    page.course_search "ENGL101"
    page.search
  end

 end


Then /^I clear the search entry$/ do
  on CourseSearch do |page|
    page.clear
  end
end

Then /^the search entry should be cleared$/ do
  on CourseSearch do |page|
    page.course_code_clear
  end
end


When /^I enter the course in the search field$/ do
  on CourseSearch do |page|
    page.course_search "ENGL101"
end
end




Then /^the course (.*) appear in search results$/ do |arg|
  on CourseSearch do |page|
    if arg == "should"
      page.results_list.should include "ENGL101"
    else
      begin
        page.results_list.should_not include "ENGL101"
      rescue Watir::Exception::UnknownObjectException
        # Implication here is that there were no search results at all.
      end
    end
  end
end