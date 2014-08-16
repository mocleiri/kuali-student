
When(/^I search for courses with multiple prefixes in the Course Search Page$/) do
  @course_search_result = make CourseSearch,
                               :search_string => "BSCI ENGL"
  @course_search_result.search :navigate=>true
end


And /^I narrow the search results to courses with available seats$/ do
  on(CourseSearchPage).select_facet("avail_seats")
end


And(/^I narrow the search results by a specific course prefix$/) do
  @course_search_result.edit :course_prefix => "ENGL"
  on(CourseSearchPage).select_facet("course_prefix",@course_search_result.course_prefix)
end




Then /^I should see only courses with available seats$/ do
  on CourseSearchPage do |page|
    # compare count displayed in the facet with actual rows displayed
    seats_avail_courses = page.seats_avail_count_number
    # add 1 because first table row is the header
    page.results_table.rows.length.should == seats_avail_courses + 1

    #page.results_table.rows[1..-1].each do |row|   Implement when details view is fully built
    #  page.row_result_link(row).click
    #  page.seats_avail.should be_true
    #  page.back_to_search_results
    #end
  end
end


Then(/^I should see only courses with the specific course prefix$/) do

  on CourseSearchPage do |page|
    page.results_table.rows[1..-1].each do |row|
      page.course_prefix_by_row(row).should match /#{@course_search_result.course_code}/
    end
  end


end


And(/^I narrow the search results by a specific course level$/) do
  @course_search_result.edit :course_level=>"400"
  on(CourseSearchPage).select_facet("course_level",@course_search_result.course_level)
end

Then /^I should see only courses with the specific course level and the specific course prefix$/ do
  on CourseSearchPage do |page|
    page.results_table.rows[1..-1].each do |row|
      page.courseLevel(row).should match /#{@course_search_result.course_level.slice(0,1)}/
      page.course_prefix_by_row(row).should match /#{@course_search_result.course_code}/
    end
  end
end

Then(/^I should see only courses with the specific course level$/) do
  on CourseSearchPage do |page|
    page.results_table.rows[1..-1].each do |row|
      page.courseLevel(row).should match /#{@course_search_result.course_level.slice(0,1)}/
    end
  end
end



When /^I narrow the search results using any facet$/ do
  @course_search_result = make CourseSearch,
                               :search_string => "ENGL" ,
                               :course_level=> '300'
  @course_search_result.search :navigate=>true
  sleep 1
  on CourseSearchPage do |page|
    @search_results_before_facet_selection=page.results_table.text
  end
  on(CourseSearchPage).select_facet("course_level",@course_search_result.course_level)
end



And(/^I undo the filtering performed using the specified facet$/) do
  on CourseSearchPage do |page|
    page.clear_facet("course_level",@course_search_result.course_level)
    sleep 2
    @search_results_after_clearing=page.results_table.text
  end

end

Then /^I should see the courses in the search results without any filtering being applied$/ do
  @search_results_before_facet_selection.should == @search_results_after_clearing
end
