When /^I search for a course with "(.*?)" text option$/ do |text|
  @course_search_result = make CourseSearch, :search_string=> text
  @course_search_result.search :navigate=>true
end

Then /^courses containing  "(.*?)" text option appears$/ do |expected|
  on CourseSearchPage do |page|
    page.results_list_courses(expected).each { |e| e.should include (expected) }
  end
end

Then /^courses containing  "(.*?)" text options appear$/ do |expected|
  on CourseSearchPage do |page|
    results = page.results_list_courses(expected)
    for result in results
      ((result-expected.split(", ")).empty?).should == true;
    end
  end
end

When /^I search for (\w+) courses on the course search page$/ do |search_string|
  @course_search_result = make CourseSearch, :search_string => search_string
  @course_search_result.search :navigate=>true
end

# DO WE NEED TO TEST THIS??
When /^I choose to see "(.*?)" records per page$/ do |per_page|
  on CourseSearchPage do |page|
    page.course_search_results_select.select per_page
    #Sleep for X seconds to wait for the js to process the change
    sleep(2)
    page.course_search_results_select.value.should == per_page
  end
end

Then /^There will be (.*?) pages of results with (.*?) records per page$/ do |pages, total_per_page|
  on CourseSearchPage do |page|
    page.result_pagination.span.links.size.to_s.should == pages

    #Doing size-1 here since results_table.rows.size includes the header row
    (page.results_table.rows.size-1).to_s.should == total_per_page
  end
end

Then /^Pagination controls will not be visible if there is only 1 page$/ do
  on CourseSearchPage do |page|
    #If there's only one page, no pagination controls or record# selection
    elementsPresent = true
    if "1" == page.result_pagination.span.links.size.to_s
      elementsPresent = false
    end

    page.result_pagination.visible?.should == elementsPresent
    page.course_search_results_select.visible?.should == elementsPresent
  end
end

When /^I sort the results by (course code|title|credits)$/ do |sort_key|
  @course_search_result.sort_results :sort_key=>sort_key
end

Then /^the course codes should be sorted in ascending order$/ do
  @course_search_result.check_sort_order_in_all_pages(:sort_key=>"course_code",:sort_order=>"A").should be_true
end

Then /^the course codes should be sorted in descending order$/ do
  @course_search_result.check_sort_order_in_all_pages(:sort_key=>"course_code",:sort_order=>"D").should be_true
end

Then /^the titles should be sorted in ascending order$/ do
  @course_search_result.check_sort_order_in_all_pages(:sort_key=>"course_title",:sort_order=>"A").should be_true
end

Then /^the titles should be sorted in descending order$/ do
  @course_search_result.check_sort_order_in_all_pages(:sort_key=>"course_title",:sort_order=>"D").should be_true
end

Then /^the credits should be sorted in ascending order$/ do
  @course_search_result.check_sort_order_in_all_pages(:sort_key=>"credits",:sort_order=>"A").should be_true
end

Then /^the credits should be sorted in descending order$/ do
  @course_search_result.check_sort_order_in_all_pages(:sort_key=>"credits",:sort_order=>"D").should be_true
end

Then /^I can view the details of the (\w+) course$/ do |course_code|
  @course_search_result.navigate_course_detail_page :course_code=>course_code
  on CourseDetailsPage do |page|
    page.details_course_description_div(course_code).wait_until_present
    page.details_course_code.should == course_code
    page.details_course_title.should =~ /Cell Biology and Physiology/i
    page.details_course_credits.should =~ /4 cr/i
    page.details_course_description(course_code).should =~ /Biochemical and physiological mechanisms/i
  end
end

When /^I select a lecture and lab$/ do
  @course_search_result.select_ao :ao_type=>"Lecture", :ao_code=>"Y"
  @course_search_result.select_ao :ao_type=>"Lab", :ao_code=>"AA"
  @course_search_result.set_section :section => "1026"       # TEMP HARDCODE
end

Then /^I should see only the selected lecture and lab$/ do
  on CourseDetailsPage do |page|
    page.selected_message.text.should match /Section(\s*)#{@course_search_result.selected_section}(\s*)/i
  end
end

When /^I add the selected lecture and lab to my registration cart$/ do
  course_options = (make CourseOptions)
  @reg_request = make RegistrationRequest, :student_id=>"student",
                      :course_code=>"BSCI330",
                      :reg_group_code=>@course_search_result.selected_section,
                      :course_options => course_options
  @reg_request.add_to_cart_from_search_details
end

Then /^I can see the selected section has been added to my cart$/ do
  on CourseDetailsPage do |page|
    page.cart_course_code(@reg_request.course_code,@reg_request.reg_group_code).wait_until_present
    expected_text = "#{@reg_request.course_code} (#{@reg_request.reg_group_code})"
    page.cart_course_code(@reg_request.course_code,@reg_request.reg_group_code).text.should == expected_text
  end

end

Given /^I have added a CHEM course to my registration cart$/ do
  @reg_request = make RegistrationRequest, :term_descr=>"Fall 2012",
                      :course_code=>"CHEM241",
                      :reg_group_code=>"1014"
  @reg_request.create_from_search
end

When /^I search for the same course$/ do
  @course_search_result = make CourseSearch, :search_string=>@reg_request.course_code,
                               :selected_section=>@reg_request.reg_group_code
  @course_search_result.search :search_string=>@reg_request.course_code
end

And /^I select the same lecture and discussion as in the course$/ do
  sleep 2
  @course_search_result.navigate_course_detail_page :course_code=>@reg_request.course_code
  on CourseDetailsPage do |page|
    page.results_table("Discussion").wait_until_present
    page.toggle_ao_select("O")
    wait_until { page.details_heading("Lecture").text =~ /Selected/i }
    page.toggle_ao_select("U")
    wait_until { page.details_heading("Discussion").text =~ /Selected/i }
  end
end

Then /^the Add to Cart option should change to a notice that the course is in my cart$/ do
  on CourseDetailsPage do |page|
    page.add_to_cart_button.present?.should == false
    page.in_cart_button.visible?.should == true
  end
end

Then /^I remove the course from my registration cart on the search page$/ do
  @reg_request.remove_from_cart_on_search
end