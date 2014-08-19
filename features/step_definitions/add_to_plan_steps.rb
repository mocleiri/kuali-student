Given /^There is an existing unplanned course$/ do
  navigate_to_maintenance_portal
  navigate_to_course_search_home
  @course_search_result = make CourseSearchResults,  :planned_term=>"2014Spring", :course_code => "ENGL206"
  @course_search_result.course_search_to_planner
end

When /^I search for a course from course search$/ do
    @course_search_result = make CourseSearchResults,  :planned_term=>"2014Spring", :course_code => "ENGL206"
    @course_search_result.set_search_entry
end

When /^I add the course with notes and term to myplan$/ do
    @course_search_result.select_add_to_plan
   end


Then /^the course should appear under current term with updated notes$/ do
  on CoursePlannerPage do |page|
     page.close_popup.wait_until_present
  #***************Checking  whether the edited notes is updated*********************************************************
     page.notes_content.should == @course_search_result.notes
     page.close_popup.wait_until_present
     page.close_popup_click
     sleep 2

  #********Steps to verify, whether the course code is saved along with updated notes in the planner********************
     @course_search_result.verify_saved_course_code_notes
     page.notes_content.should == @course_search_result.notes
     page.close_popup_click

  end
end

Given /^I work on scheduled planner$/ do
     @course_search_result = make CourseSearchResults, :planned_term=>"2014Spring",:course_code => "ENGL206"
     @course_search_result.add_course_to_term
end


 When /^I edit the notes of a course under a current term$/ do
     @course_search_result = make CourseSearchResults,  :planned_term=>"2014Spring",:course_code => "ENGL206"
     @course_search_result.edit_plan_item
 end



Then  /^the course with notes appears under the term on the planner$/ do
   on CoursePlannerPage do |page|
       #*********** Checking whether the information icon exists?************
        page.info_icon(@course_search_result.planned_term, @course_search_result.course_code).exists?.should == true
        page.course_code_term_click(@course_search_result.planned_term, @course_search_result.course_code)
        page.view_course_summary_click
        page.close_popup.wait_until_present
        page.notes_content.should == @course_search_result.notes
        page.close_popup_click

     end
end

#New steps


When(/^I search for a course\(CM\) from course search$/) do
  @course_search_result = make CourseSearchResults,  :planned_term=>"2014Fall", :course_code => "ENGL206", :term=>"Fall 2014"
  @course_search_result.course_search
end



And(/^I add the course\(CM\) from Search to the Planned section for a sepcific term$/) do
  on CourseSearch do |page|
    page.plan_page_click
  end
  sleep 15

  on CoursePlannerPage do |page|

    begin
      #page.course_planner_header.wait_until_present
      sleep 5
      page.course_code_term(@course_search_result.planned_term, @course_search_result.course_code) != nil?
      puts page.course_code_term(@course_search_result.planned_term, @course_search_result.course_code)
      page.course_code_term_click(@course_search_result.planned_term, @course_search_result.course_code)
      page.course_code_delete_click
      page.delete_course.wait_until_present
      page.delete_course_click
      page.refresh
    rescue
      #means that course was NOT found, BUT be careful as the rescue will hide errors if they occur in cleanup steps
    end


    begin
      #page.course_planner_header.wait_until_present
      sleep 5
      if   page.course_code_term_backup(@course_search_result.planned_term, @course_search_result.course_code) != nil?
        puts page.course_code_term_backup(@course_search_result.planned_term, @course_search_result.course_code)
        page.course_code_term_click_backup(@course_search_result.planned_term, @course_search_result.course_code)
        page.course_code_delete_click
        page.delete_course.wait_until_present
        page.delete_course_click
        page.refresh
      end

    rescue
      #   puts "the rescuer"
      #means that course was NOT found, BUT be careful as the rescue will hide errors if they occur in cleanup steps
    end
  end

  navigate_to_course_search_home
  @course_search_result.select_add_to_plan
end


Then(/^the course should be there in the Planner$/) do
  navigate_to_course_planner_home
  sleep 15
  on CoursePlannerPage do |page|
  page.course_code_term(@course_search_result.planned_term, @course_search_result.course_code)==@course_search_result.course_code
  end
end


And(/^I add the course\(CM\) from the CDP details page$/) do
  on CourseDetailPage do |page|
    page.add_to_plan.click
    sleep 15
    page.term_cdp.select @course_search_result.term
    page.add_to_plan_notes_cdp.set @course_search_result.notes
    page.add_to_plan_button_cdp
  end
end



When(/^I navigate to the Course Section Details$/) do
  #navigate to course search
  @course_search_result = make CourseSearchResults,  :planned_term=>"2014Fall", :course_code => "ENGL206", :term=>"Fall 2014"
  @course_search_result.course_search

  #navigate to planner page
  on CourseSearch do |page|
    page.plan_page_click
  end
  sleep 15

  #delete an existing course
  on CoursePlannerPage do |page|

    begin
      #page.course_planner_header.wait_until_present
      sleep 5
      page.course_code_term(@course_search_result.planned_term, @course_search_result.course_code) != nil?
      puts page.course_code_term(@course_search_result.planned_term, @course_search_result.course_code)
      page.course_code_term_click(@course_search_result.planned_term, @course_search_result.course_code)
      page.course_code_delete_click
      page.delete_course.wait_until_present
      page.delete_course_click
      page.refresh
    rescue
      #means that course was NOT found, BUT be careful as the rescue will hide errors if they occur in cleanup steps
    end


    begin
      #page.course_planner_header.wait_until_present
      sleep 5
      if   page.course_code_term_backup(@course_search_result.planned_term, @course_search_result.course_code) != nil?
        puts page.course_code_term_backup(@course_search_result.planned_term, @course_search_result.course_code)
        page.course_code_term_click_backup(@course_search_result.planned_term, @course_search_result.course_code)
        page.course_code_delete_click
        page.delete_course.wait_until_present
        page.delete_course_click
        page.refresh
      end

    rescue
      #   puts "the rescuer"
      #means that course was NOT found, BUT be careful as the rescue will hide errors if they occur in cleanup steps
    end
  end

  #navigate to course search

  @course_search_result = make CourseSearchResults,  :planned_term=>"2014Fall", :course_code => "ENGL206", :term=>"Fall 2014"
  navigate_to_course_search_home
  #@course_search_result.course_search


  sleep 15
  #navigate to course details page
  @course_search_result.navigate_course_detail_page
  on CourseSectionPage do |page|
    page.course_termlist.wait_until_present(120)
  end




end



And(/^I add the course\(CM\) from Search to the Backup section for a sepcific term$/) do
  on CourseSearch do |page|
    page.plan_page_click
  end
  sleep 15
  on CoursePlannerPage do |page|

    begin
      #page.course_planner_header.wait_until_present
      sleep 5
      page.course_code_term(@course_search_result.planned_term, @course_search_result.course_code) != nil?
      puts page.course_code_term(@course_search_result.planned_term, @course_search_result.course_code)
      page.course_code_term_click(@course_search_result.planned_term, @course_search_result.course_code)
      page.course_code_delete_click
      page.delete_course.wait_until_present
      page.delete_course_click
      page.refresh
    rescue
      #means that course was NOT found, BUT be careful as the rescue will hide errors if they occur in cleanup steps
    end


    begin
      #page.course_planner_header.wait_until_present
      sleep 5
      if   page.course_code_term_backup(@course_search_result.planned_term, @course_search_result.course_code) != nil?
        puts page.course_code_term_backup(@course_search_result.planned_term, @course_search_result.course_code)
        page.course_code_term_click_backup(@course_search_result.planned_term, @course_search_result.course_code)
        page.course_code_delete_click
        page.delete_course.wait_until_present
        page.delete_course_click
        page.refresh
      end

    rescue
      #   puts "the rescuer"
      #means that course was NOT found, BUT be careful as the rescue will hide errors if they occur in cleanup steps
    end
  end

  navigate_to_course_search_home
  @course_search_result.select_add_to_plan_backup

end



Then(/^the course should be there in the backup section of the planner$/) do
  navigate_to_course_planner_home
  sleep 15
  on CoursePlannerPage do |page|
    page.course_code_term_backup(@course_search_result.planned_term, @course_search_result.course_code)==@course_search_result.course_code
  end
end

When(/^I search for a course\(CM\)$/) do
  @course_search_result = make CourseSearchResults,  :planned_term=>"2014Fall", :course_code => "ENGL206", :term=>"Fall 2014"
  @course_search_result.course_search
end



And(/^I add the course\(CM\) from the CDP details page to the Backup section$/) do
  on CourseDetailPage do |page|
    page.add_to_plan.click
    sleep 15
    page.term_cdp.select @course_search_result.term
    page.add_to_plan_notes_cdp.set @course_search_result.notes
    page.backup_checkbox_cdp.set
    page.add_to_plan_button_cdp
  end
end

Then(/^the course should be there in the Backup section of the planner$/) do
  navigate_to_course_planner_home
  sleep 15
  on CoursePlannerPage do |page|
    page.course_code_term_backup(@course_search_result.planned_term, @course_search_result.course_code)==@course_search_result.course_code
  end
end