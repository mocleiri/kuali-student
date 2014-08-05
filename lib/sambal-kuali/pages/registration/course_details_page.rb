class CourseDetailsPage < RegisterForCourseBase

  expected_element :course_credits_div

  # return to search
  element(:return_to_search_link) { |b| b.link(id: "returnToSearch") }
  action(:return_to_search) { |b| b.return_to_search_link.click }

  # header
  element(:course_code) { |b| b.span(id: "search_details_code").text }
  element(:course_title) { |b| b.span(id: "search_details_title").text }
  element(:course_credits_div) { |b| b.span(id: "search_details_credits") }
  element(:course_credits) { |b| b.course_credits_div.text }

  # description
  element(:course_description_div) { |course_code,b| b.div(id: "courseDescription_#{course_code}") }
  element(:course_description) { |course_code,b| b.course_description_div(course_code).text }

  # heading
  element(:details_heading) { |activity_type, b| b.div(id: "#{activity_type}_details_heading") }

  # selected messages
  element(:selected_message) { |b| b.div(id: "one_ao_selected") }

  # activities
  element(:results_table) { |activity_type,b| b.table(id: "#{activity_type}_search_results_table") }
  element(:table_row) { |activity_type, ao_code, b| b.results_table(activity_type).row(id: "course_detail_row_#{ao_code}") }
  element(:select_box) { |ao_code, b| b.span(id: "select_#{ao_code}") }
  action(:toggle_ao_select) { |ao_code, b| b.select_box(ao_code).click }

  # Detail AO table column indexes
  AO_DAYS = 0
  AO_TIME = 1
  AO_INSTRUCTOR = 2
  AO_LOCATION = 3
  AO_SEATS = 4
  AO_SELECT = 5

end