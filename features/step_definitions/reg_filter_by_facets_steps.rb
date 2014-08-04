When /^I search for (\w+) courses in the Course Search Page$/ do  |course_code|
  @course_search_result = make CourseSearch,
                               :course_code => course_code ,
                               :scheduled_terms=>nil,
                               :gened_requirements=>nil,
                               :subject=>nil,
                               :gened_code=>nil,
                               :credit=>nil
  p @course_search_result
  @course_search_result.course_search
  @course_search_result.clear_facets
end

When(/^I search for courses with multiple prefixes in the Course Search Page$/) do
  @course_search_result = make CourseSearch,
                               :course_code => "BSCI ENGL" ,
                               :scheduled_terms=>nil,
                               :gened_requirements=>nil,
                               :subject=>nil,
                               :gened_code=>nil,
                               :credit=>nil
  p @course_search_result
  @course_search_result.course_search
  @course_search_result.clear_facets
  #for facets, pick one prefix
  @course_search_result.course_code = "ENGL"
end


And /^I narrow the search results to courses with available seats$/ do
  on CourseSearchPage do |page|
    @course_search_result.select_facet("avail_seats")
    # page.clear_term_facet.wait_until_present
    #cannot avoid sleep currently since have already tried to wait for element existence.Can research in future if there is a way
    sleep 1
  end
end


And(/^I narrow the search results by a specific course prefix$/) do

  on CourseSearchPage do |page|
    @course_search_result.select_facet("course_prefix")
    page.clear_prefix_facet.wait_until_present
    #cannot avoid sleep currently since have already tried to wait for element existence.Can research in future if there is a way
    sleep 1
  end
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
  on CourseSearchPage do |page|
    @course_search_result.select_facet("course_level")
    page.clear_level_facet.wait_until_present
    #cannot avoid sleep currently since have already tried to wait for element existence.Can research in future if there is a way
    sleep 1
  end
end

And /^I narrow the search results by a specific course level and a specific course prefix$/ do
  on CourseSearchPage do |page|
    @course_search_result.select_facet("course_level")
    page.clear_level_facet.wait_until_present
    sleep 1
    @course_search_result.select_facet("course_prefix")
    page.clear_prefix_facet.wait_until_present
    sleep 1
    #things
  end
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
                               :course_code => "ENGL" ,
                               :scheduled_terms=>"SP 14",
                               :gened_requirements=>"General",
                               :subject=>"English",
                               :gened_code=>"DSHU",
                               :credit=>"1",
                               :course_level=> '300',
                               :course_prefix=>'ENGL'
  @course_search_result.course_search
  @course_search_result.clear_facets
  sleep 1
  on CourseSearchPage do |page|
    @search_results_before_facet_selection=page.results_table.text
    @course_search_result.select_facet("course_level")
    page.clear_level_facet.wait_until_present
    #cannot avoid sleep currently since have already tried to wait for element existence.Can research in future if there is a way
    sleep 1
  end
end



And(/^I undo the filtering performed using the specified facet$/) do
  on CourseSearchPage do |page|

    page.clear_level_facet.click
    #cannot avoid sleep currently since have already tried to wait for element existence.Can research in future if there is a way
    sleep 1
    @search_results_after_clearing=page.results_table.text
  end

end

Then(/^I should see the courses in the search results without any filtering being applied$/) do
  @search_results_before_facet_selection.should == @search_results_after_clearing
end
