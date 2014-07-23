class RegisterForCourseSearch < RegisterForCourseBase
  page_url "#{$test_site}/registration/index.jsp#/search"
  expected_element :cr_header_div

  element(:course_input_div){ |b| b.div(class: "kscr-Responsive-searchFormWrapper kscr-SearchForm") }
  element(:course_input){ |b| b.text_field(id: "courseSearchCriteria") }
  element(:course_input_button) { |b| b.button(id: "searchSubmit") }
  action(:begin_course_search) { |b| b.course_input_button.click}

  def search_for_a_course(course)
    course_input.set course
    begin_course_search
  end
end