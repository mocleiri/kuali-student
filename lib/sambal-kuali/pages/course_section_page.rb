class CourseSectionPage < BasePage

  page_url "#{$test_site}kr-krad/inquiry"

  wrapper_elements
  frame_element
  expected_element :course_detail_header

 #10-Elements in the course section
  element(:course_detail_header) { |b| b.main(id:"coursedetails-page").span(class:"uif-headerText-span")}
  value(:term_and_course_offering){ |b|b.a(id:"kuali-atp-2014Spring_section_toggle").text }
  element(:term_and_course){ |b|b.a(id:"u128z5dt_line0_toggle")}
  element(:course_offering){ |b|b.section(id:"u11k4e1o_line0_line0").header(id:"u1rmsfyv_line0_line0")}
  element(:course_description){ |b|b.div(id:"courseOfferingDescription_line0_line0")}
  value(:activity_offering_code){ |activity_sub_level,activity_level,course_level,course_section_level,format_level,b| b.div(id:"code_line#{course_section_level}_line#{course_level}_line#{format_level}_line#{activity_level}_line#{activity_sub_level}").text}
  value(:activity_offering_instructor) { |activity_sub_level,activity_level,course_level,course_section_level,format_level,b| b.div(id:"instructor_line#{course_section_level}_line#{course_level}_line#{format_level}_line#{activity_level}_line#{activity_sub_level}").text}
  value(:activity_offering_days){ |activity_sub_level,activity_level,course_level,course_section_level,format_level,b| b.div(id:"days_line#{course_section_level}_line#{course_level}_line#{format_level}_line#{activity_level}_line#{activity_sub_level}").text}
  value(:activity_offering_time){ |activity_sub_level,activity_level,course_level,course_section_level,format_level,b| b.div(id:"time_line#{course_section_level}_line#{course_level}_line#{format_level}_line#{activity_level}_line#{activity_sub_level}").text}
  value(:activity_offering_location){ |activity_sub_level,activity_level,course_level,course_section_level,format_level,b| b.div(id:"location_line#{course_section_level}_line#{course_level}_line#{format_level}_line#{activity_level}_line#{activity_sub_level}").text}
  value(:activity_offering_seats){ |activity_sub_level,activity_level,course_level,course_section_level,format_level,b| b.div(id:"seats_line#{course_section_level}_line#{course_level}_line#{format_level}_line#{activity_level}_line#{activity_sub_level}").text}
  value(:activity_offering_additional_details){ |activity_sub_level,activity_level,course_level,course_section_level,format_level,b|b.div(id:"additionalDetails_line#{course_section_level}_line#{course_level}_line#{format_level}_line#{activity_level}_line#{activity_sub_level}").text}
  value(:activity_offering_select){ |activity_sub_level,activity_level,course_level,course_section_level,format_level,b|b.div(id:"select _line#{course_section_level}_line#{course_level}_line#{format_level}_line#{activity_level}_line#{activity_sub_level}").text}




  #Format Offering Elements
 # element(:lecture_lab_discussion){|course_code,b|b.}

  action(:lecture_lab_discussion) { |course_code,b| b.div(id: /#{course_code}_formatOfferingOptions/).radio(index:0) }
  action(:lecture) { |course_code,b| b.div(id: /#{course_code}_formatOfferingOptions/).radio(index:1) }
  action(:ao_lecture){|course_code,b| b.div(id: /#{course_code}.*activity-offering-lecture/) }
  action(:ao_discussion){|course_code,b| b.div(id: /#{course_code}.*activity-offering-discussion/) }
  action(:ao_lab){|course_code,b| b.div(id: /#{course_code}.*activity-offering-lab/) }
end

