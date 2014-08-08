class CourseSearchMobilePage < RegisterForCourseBase

  def go_to_results_page (search_string)
    page_url = "#{$test_site}/registration/index.jsp#/search/#{search_string}"
    @browser.goto page_url
    course_input_div.wait_until_present
  end

  # Search input
  element(:course_input_div){ |b| b.div(class: "kscr-Responsive-searchFormWrapper kscr-SearchForm") }
  element(:course_input){ |b| b.text_field(id: "courseSearchCriteria") }
  element(:course_input_button) { |b| b.button(id: "searchSubmit") }
  action(:begin_course_search) { |b| b.course_input_button.click}

  def search_for_a_course(course)
    course_input.set course
    begin_course_search
  end

  # Facets
  element(:seats_avail_toggle) { |b| b.li(id: "search_facet_seatsAvailable_option_seatsAvailable") }
  action(:toggle_seats_avail) { |b| b.seats_avail_toggle.click }
  element(:seats_avail_count) { |b| b.seats_avail_toggle.span(index: 1).text }
  element(:credits_toggle) { |credits, b| b.li(id: "search_facet_creditOptions_option_#{credits}") }
  action(:toggle_credits) { |credits, b| b.credits_toggle(credits).click }
  element(:course_level_toggle) { |course_level,b| b.li(id: "search_facet_courseLevel_option_#{course_level}") }
  action(:toggle_course_level) { |course_level,b| b.course_level_toggle(course_level).click }
  element(:course_prefix_toggle) { |course_prefix,b| b.li(id: "search_facet_coursePrefix_option_#{course_prefix}") }
  action(:toggle_course_prefix) { |course_prefix,b| b.course_prefix_toggle(course_prefix).click }
  element(:clear_level_facet) { |b| b.div(id: "search_facet_clear_courseLevel") }
  element(:clear_prefix_facet) { |b| b.div(id: "search_facet_clear_coursePrefix") }
  element(:clear_credit_facet) { |b| b.div(id: "search_facet_clear_creditOptions") }

  # Results

  element(:results_list){ |b| b.div(id: "search-results-list")}
  element(:result_item) { |course_code,b| b.div(id: "course_detail_result_#{course_code}")}
  element(:result_item_courseCode) { |course_code,b| b.div(id: "search-result-column-#{course_code}-courseCode")}
  element(:result_item_longName) { |course_code,b| b.div(id: "search-result-column-#{course_code}-longName")}
  element(:result_item_credits) { |course_code,b| b.div(id: "search-result-column-#{course_code}-creditOptions")}


  #click on a card to go to details view
  def select_course(course_code)
    result_item(course_code).click
  end

end

