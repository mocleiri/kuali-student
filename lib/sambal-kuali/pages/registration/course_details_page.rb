class CourseDetailsPage < RegisterForCourseBase

  # return to search
  element(:return_to_search_link) { |b| b.link(id: "returnToSearch") }
  action(:return_to_search) { |b| b.return_to_search_link.click }

  # header
  element(:course_code) { |course_code,b| b.span(id: "search_details_code_#{course_code}").text }
  element(:course_title) { |course_code,b| b.span(id: "search_details_title_#{course_code}").text }
  element(:course_credits) { |course_code,b| b.span(id: "search_details_credits_#{course_code}").text }

  # description
  element(:course_description_div) { |course_code,b| b.div(id: "courseDescription_#{course_code}") }
  element(:course_description) { |course_code,b| b.div(id: "courseDescription_#{course_code}").text }

end