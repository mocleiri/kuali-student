
Then(/^I should see a gutter for bookmarks$/) do
 on CoursePlannerPage do |page|
   page.bookmark_gutter.exists?.should==true
 end
end



When(/^I navigate to the Planner page$/) do
  navigate_to_course_planner_home
 end


And(/^I click on View more details link$/) do

  on CoursePlannerPage do |page|
    sleep 1
 page.view_more_details.click
  end
end


Then(/^I should be able to see a page that displays the bookmarks and display the CDP overview section information$/) do
on BookmarkPage do |page|
  sleep 2
  page.bookmark_page.exists?.should==true
  page.bookmark_details.exists?.should==true
end
end


When(/^I bookmark a course$/) do

  @course_search_result = make CourseSearchResults,:course_code => "ENGL201"
  @course_search_result.course_search
  #Search for a course
  @course_search_result.navigate_course_detail_page

  on CourseDetailPage do |page|
    sleep 2
    puts page.removebookmark.exists?
    if page.removebookmark.exists? then
      puts page.removebookmark.exists?
      else
      page.add_bookmark
    end
  end




end
Then(/^I should be able to view a link to bookmark page in the secondary navigation$/) do
  on BookmarkPage do |page|
    page.browser_secondary_nav.exists?
  end
end

And(/^I bookmark the course$/) do
  on CourseSearch do |page|
    if page.boomark_icon_empty.exists? then
       page.bookmark_icon_empty.click
    else
      page.bookmark_icon.click
    end
  end
end

Then(/^I should be able to bookmark the course and remove the bookmark$/) do
  on CourseSearch do |page|
    if page.bookmark_icon_empty(@course_search_results.course_code).exists? then
      page.bookmark_icon_empty(@course_search_results.course_code).click
    else
      page.bookmark_icon(@course_search_results.course_code).click
    end
  end
end



Then(/^I should be able to remove the bookmark in the course details page$/) do
  on CourseDetailPage do |page|
    sleep 2
    page.removebookmark.click
    sleep 5
    page.remove_bookmark_message.exists?.should==true
    end
end


Then(/^I should be able to bookmark  the course$/) do
  on CourseSearch do |page|

  if page.star_remove_bookmark.exists? then
    page.star_remove_bookmark
  else
    page.star_bookmark.click
    sleep 5
    page.bookmark_message.exists?.should==true
  end

end
end


Then(/^I should be able to remove the bookmark for the course$/) do
  on CourseSearch do |page|

    if page.star_remove_bookmark.exists? then
      page.star_remove_bookmark
    else
      page.star_bookmark.click
      sleep 2
      page.star_remove_bookmark.click
    end

  end

end