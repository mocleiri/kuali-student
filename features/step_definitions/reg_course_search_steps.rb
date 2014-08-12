When /^I search for a course$/ do
  @course_search_result = make CourseSearch
  @course_search_result.course_search
end


When /^I search for a course with "(.*?)" text option$/ do |text|
  @course_search_result = make CourseSearch
  @course_search_result.course_search(text)
end

Then /^the course "(.*?)" appear in the search results$/ do |test_condition|
  on CourseSearchPage do |page|
    if test_condition == "should"
      page.results_list.should include @course_search_result.course_code
    else
      begin
        page.results_list.should_not include @course_search_result.course_code
      rescue Watir::Exception::UnknownObjectException
        # Implication here is that there were no search results at all.
      end
    end
  end
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

When /^I search for a "(.*?)" "(.*?)" by "(.*?)"$/ do |course_status,course, term_selection|
  @course_search_result = make CourseSearch, :course_code => course, :term_select => term_selection
  @course_search_result.course_search
end



#-------------------------------------------------------------------------------------------

When /^I search for (\w+) courses on the course search page$/ do |search_string|
  @course_search_result = make CourseSearch, :course_code => search_string
  @course_search_result.course_search
  @course_search_result.clear_facets
end


When /^I search for a course on the course search page with course title$/ do
  @course_search_result = make CourseSearch, :search_text => "eng"
  @course_search_result.course_search_with_search_text
end


When /^I search for a course with one word"(.*?)" text option$/ do |text|
  @course_search_result = make CourseSearch, :search_text => text
  @course_search_result.course_search_with_search_text
end

Then /^course title or course description containing "(.*?)"text option "(.*?)" appear$/ do |text,condition|
  @course_search_result = make CourseSearch
  if condition == "should"
    @course_search_result.multi_text_search(text).should be_true
  else
    begin
      @course_search_result.multi_text_search(text).should_not be_true
    rescue Watir::Exception::UnknownObjectException
      # Implication here is that there were no search results at all.
    end
  end
end

When /^I search for a course with multi word"(.*?)" text option$/ do |text|
  @course_search_result = make CourseSearch, :search_text => text, :term_select => "All"
  @course_search_result.course_search_with_search_text

end


Then(/^course code or course title or course description containing any word of "(.*?)"text option "(.*?)" appear$/) do |expected, condition|
  @course_search_result = make CourseSearch
  if condition == "should"
    @course_search_result.multi_text_search(expected).should be_true
  else
    begin
      @course_search_result.multi_text_search(expected).should_not be_true
    rescue Watir::Exception::UnknownObjectException
      # Implication here is that there were no search results at all.
    end
  end
end

#------------------------------------------------------------------------------------------------------------------------------------

When /^I search for a course with "(.*?)" level option$/ do |level|
  @course_search_result = make CourseSearch
  @course_search_result.course_search(level)
end

Then /^only "(.*?)" level courses "(.*?)" be displayed$/ do |text, condition|
  @course_search_result = make CourseSearch
  if condition == "should"
    @course_search_result.check_all_results_data_for_level(text).should be_true
    puts "Test is Passed True"
  else
    begin
      @course_search_result.check_all_results_data_for_level(text).should_not be_true
    rescue Watir::Exception::UnknownObjectException
    end
  end
end


#------------------------------------------------------------------------------------------------------------------------------------
When /^I search for "(.*?)"$/ do |text|
  @course_search_result = make CourseSearch, :search_text => text
  @course_search_result.course_search_with_search_text
end


Then /^"(.*?)" and courses matching at least one "(.*?)" are returned$/ do |expected_courses, expected_component|
  @course_search_result.check_all_results_data(expected_courses,expected_component).should be_true
end



When /^I sort the results by (course code|title|credits)$/ do |sort_key|
  column = case sort_key
             when "course code" then "Code"
             when "title" then "Title"
               when "credits" then "Credits"
           end
  on CourseSearchPage do |page|
    wait_until {page.sort_selector(column).visible?}
    page.sort_results_by(column)
  end
end

Then /^the course codes should be sorted in ascending order$/ do
  @course_search_result.check_code_ascending_order_in_all_pages.should be_true
end

Then /^the course codes should be sorted in descending order$/ do
  @course_search_result.check_code_descending_order_in_all_pages.should be_true
end

Then /^the titles should be sorted in ascending order$/ do
  @course_search_result.check_title_ascending_order_in_all_pages.should be_true
end

Then /^the titles should be sorted in descending order$/ do
  @course_search_result.check_title_descending_order_in_all_pages.should be_true
end

Then /^the credits should be sorted in ascending order$/ do
  @course_search_result.check_credits_ascending_order_in_all_pages.should be_true
end

Then /^the credits should be sorted in descending order$/ do
  @course_search_result.check_credits_descending_order_in_all_pages.should be_true
end

#-----------------------------------------------------------------------------------------------------------------------

When /^I search for a course with "(.*?)" option$/ do |text|
  @course_search_result = make CourseSearch, :search_text => text
  @course_search_result.course_search_with_search_text
end

Then /^the "(.*?)" and courses matching at least one "(.*?)" are returned$/ do |expected_courses, expected_component|
  @course_search_result.check_all_results_data(expected_courses,expected_component).should be_true
end


Then /^course code or course title or course description containing any text of "(.*?)" text option are returned$/ do |expected_courses, expected_component|
  @course_search_result.check_all_results_data(expected_courses,expected_component).should be_true
end


#KSAP-1000-----------------------------------------------------------------------------------------------------------------------


When /^I search for a "(.*?)" having divisions and levels with space in the search text$/ do |text|
  @course_search_result = make CourseSearch, :search_text => text
  @course_search_result.course_search_with_search_text
end


Then  /^the courses containing the "(.*?)" should be displayed in logical order$/ do |text|
  @course_search_result.logical_order_sort(text).should be_true
end


When /^I click on the course details link for (\w+)$/ do |course_code|
  @course_search_result.navigate_course_detail_page course_code
end

Then /^I can view the details of the (\w+) course$/ do |course_code|
  on CourseDetailsPage do |page|
    page.details_course_description_div(course_code).wait_until_present
    page.details_course_code.should == course_code
    page.details_course_title.should =~ /Cell Biology and Physiology/i
    page.details_course_credits.should =~ /4 cr/i
    page.details_course_description(course_code).should =~ /Biochemical and physiological mechanisms/i
  end
end

When /^I select a lecture and lab$/ do
  on CourseDetailsPage do |page|
    page.toggle_ao_select("Y")
    wait_until { page.details_heading("Lecture").text =~ /Selected/i }
    page.toggle_ao_select("AA")
    wait_until { page.details_heading("Lab").text =~ /Selected/i }
  end
end

Then /^I should see only the selected lecture and lab$/ do
  on CourseDetailsPage do |page|
      page.selected_message.text.should match /Section(\s+)#{@course_search_result.selected_section}(\s+)Selected/i
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
  @reg_request = make RegistrationRequest, :student_id=>"student",
                      :term_code=>"201208",
                      :term_descr=>"Fall 2012",
                      :course_code=>"CHEM241",
                      :reg_group_code=>"1014"

  @reg_request.create_from_search
end

When /^I search for the same course$/ do
  @course_search_result = make CourseSearch, :selected_section=>@reg_request.reg_group_code
  @course_search_result.course_search(@reg_request.course_code)
end

And /^I select the same lecture and discussion as in the course$/ do
  @course_search_result.navigate_course_detail_page @reg_request.course_code
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