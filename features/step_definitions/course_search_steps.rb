When /^I search for a course$/ do
  @course_offering = make CourseOffering
  @course_offering.course_search
end

When /^I enter the course in the search field$/ do
  @course_offering = make CourseOffering
  @course_offering.set_search_entry

end
When /^I search for a course with "(.*?)" text option$/ do |text|
  @course_offering = make CourseOffering
   @course_offering.various_input_option(text)
end


Then /^I clear the search entry$/ do
@course_offering.clear_search
end

Then /^the search entry should be cleared successfully$/ do
  on CourseSearch do |page|
       page.search_for_course.text.should == ""
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


Then /^courses containing  "(.*?)" text option appears$/ do |verify|
  on CourseSearch do |page|
    final= Array.new
    present = Array.new
    puts page.results_list
    final<<page.results_list.map! {|x| x.slice(0,4) }
 # final<< page.results_list.map! {|x| x.str{(verify),7}}
  #This step converts all contents into uppercase
  #final.map! {|x| x.upcase }

    final.each {|e| puts e
    if e.include? "ENGL"
      puts true
    else
      puts false
    end

    }

   end
 end


And /^the search results list should be cleared successfully$/ do
  on CourseSearch do |page|
    page.results_list.should == []
  end
end
