When /^I search for a course on course search page$/ do
 @course_search_result = make CourseSearchResults, :course_code => "ENGL201" ,:description=>"historical",:requisite=>"None",:scheduled_terms=>"SP 14",:projected_terms=>"Check",:gened_requirements=>"General",:subject=>"English"
 @course_section_object=make CourseSectionObject
 @course_search_result.course_search
end

When /^I search for a certain course on course search page$/ do
  @course_search_result = make CourseSearchResults, :course_code => "ENGL202" ,:description=>"historical",:requisite=>"None",:scheduled_terms=>"SP 14",:projected_terms=>"Check",:gened_requirements=>"General",:subject=>"English"
  @course_activityoffering_object=make CourseActivityOfferingObject

  @course_search_result.course_search
end




When(/^I search for course which has format offerings on the course search page$/) do
  @course_search_result = make CourseSearchResults, :course_code => "CHEM237"
  @course_activityoffering_object_1=make CourseActivityOfferingObject,
                                         :activity_offering_code => 'A',
                                         :activity_offering_days =>'MWF',
                                         :activity_offering_instructor => 'HOFFMAN, DANIEL',
                                         :activity_offering_time => '09:00 AM - 09:50 AM',
                                         :activity_offering_location=>'CHM 1224',
                                         :activity_offering_seats =>'0/36'

  @course_activityoffering_object_2=make CourseActivityOfferingObject,
                                         :activity_offering_code => 'C',
                                         :activity_offering_days =>'T',
                                         :activity_offering_instructor => 'PRESTON, NANCI',
                                         :activity_offering_time => '02:00 PM - 02:50 PM',
                                         :activity_offering_location=>'CHM 0124',
                                         :activity_offering_seats =>'0/18'

  @course_activityoffering_object_3=make CourseActivityOfferingObject,
                                         :activity_offering_code => 'B',
                                         :activity_offering_days =>'H',
                                         :activity_offering_instructor => 'DANIELS, CRAIG',
                                         :activity_offering_time => '08:00 AM - 10:50 AM',
                                         :activity_offering_location=>'CHM 1360',
                                         :activity_offering_seats =>'0/18'
  @course_search_result.course_search
end


And(/^I view the details of the course on the Course Details page$/) do
@course_search_result.navigate_course_detail_page
end

Then /^I should be able to view all the required information on the Course Details page$/ do
  on CourseDetailPage do |page|
    course_code=@course_search_result.course_code
    page.course_description(course_code).text.should match /#{@course_search_result.description}/
    page.course_requisites(course_code).text.should match /#{@course_search_result.requisite}/
    page.scheduled_terms(course_code).text.should match /#{@course_search_result.scheduled_terms}/
    page.projected_terms(course_code).text.should match /#{@course_search_result.projected_terms}/
    page.gened_requirements(course_code).text.should match /#{@course_search_result.gened_requirements}/
    page.subject(course_code).text.should match /#{@course_search_result.subject}/
  end
end


And(/^I navigate to the Course Section Details page$/) do

  @course_search_result.navigate_course_detail_page
  @course_search_result.navigate_course_section_page
  sleep 50

end

Then(/^I should be able to view the section details about the course$/) do
  on CourseSectionPage do |page|

    page.course_detail_header.text.should match /#{@course_section_object.course_detail_header}/
    page.term_and_course_offering.should == @course_section_object.term_and_course_offering
    page.course_description.text.should match /#{@course_section_object.course_description}/
    page.activity_offering_code(0,0,0,0,0).should match /#{@course_activityoffering_object.activity_offering_code}/
    page.activity_offering_instructor(0,0,0,0,0).should match /#{@course__activityoffering_object.activity_offering_instructor}/
    page.activity_offering_time(0,0,0,0,0).should match /#{@course_activityoffering_object.activity_offering_time}/
    page.activity_offering_location(0,0,0,0,0).should match /#{@course_activityoffering_object.activity_offering_location}/
    page.activity_offering_seats(0,0,0,0,0).should match /#{@course_activityoffering_object.activity_offering_seats}/

  end
end

Then(/^I should be able to view the format offerings for the course$/) do
  on CourseSectionPage do |page|
    course_code=@course_search_result.course_code
#    sleep 5
    page.lecture_lab_discussion(course_code).exists?.should==true
    page.lecture(course_code).exists?.should==true
    page.lecture_lab_discussion(course_code).click
    page.ao_lecture(course_code).exists?.should==true
    page.ao_discussion(course_code).exists?.should==true
    page.ao_lecture(course_code).exists?.should==true

    page.activity_offering_code(0,0,0,0,0).should match /#{@course_activityoffering_object_1.activity_offering_code}/
    page.activity_offering_instructor(0,0,0,0,0).should match /#{@course_activityoffering_object_1.activity_offering_instructor}/
    page.activity_offering_days(0,0,0,0,0).should match /#{@course_activityoffering_object_1.activity_offering_days}/
    page.activity_offering_time(0,0,0,0,0).should match @course_activityoffering_object_1.activity_offering_time
    page.activity_offering_location(0,0,0,0,0).should match /#{@course_activityoffering_object_1.activity_offering_location}/
    page.activity_offering_seats(0,0,0,0,0).should match /#{@course_activityoffering_object_1.activity_offering_seats}/

   #Validate the components for Lab
    page.activity_offering_code(0,1,0,0,0).should match /#{@course_activityoffering_object_2.activity_offering_code}/
    page.activity_offering_instructor(0,1,0,0,0).should match /#{@course_activityoffering_object_2.activity_offering_instructor}/
    page.activity_offering_days(0,1,0,0,0).should match /#{@course_activityoffering_object_2.activity_offering_days}/
    page.activity_offering_time(0,1,0,0,0).should match @course_activityoffering_object_2.activity_offering_time
    page.activity_offering_location(0,1,0,0,0).should match /#{@course_activityoffering_object_2.activity_offering_location}/
    page.activity_offering_seats(0,1,0,0,0).should match /#{@course_activityoffering_object_2.activity_offering_seats}/
   #Validate the components for Lab
    page.activity_offering_code(0,2,0,0,0).should match /#{@course_activityoffering_object_3.activity_offering_code}/
    page.activity_offering_instructor(0,2,0,0,0).should match /#{@course_activityoffering_object_3.activity_offering_instructor}/
    page.activity_offering_days(0,2,0,0,0).should match /#{@course_activityoffering_object_3.activity_offering_days}/
    page.activity_offering_time(0,2,0,0,0).should match @course_activityoffering_object_3.activity_offering_time
    page.activity_offering_location(0,2,0,0,0).should match /#{@course_activityoffering_object_3.activity_offering_location}/
    page.activity_offering_seats(0,2,0,0,0).should match /#{@course_activityoffering_object_3.activity_offering_seats}/



  end
end



When(/^I search for a course with Multiple Activity Offerings$/) do
  @course_search_result = make CourseSearchResults, :course_code => "CHEM231"
  @course_section_object=make CourseSectionObject
  @course_offer_object=make CourseSearchResults
  @course_search_result.course_search
  end




Then(/^I should be able to add multiple activity   offerings to my Plan$/) do
  sleep 10
  puts "#{@course_offer_object.course_offering_description_list[0].course_term_list[0].courseterm_level}"

end