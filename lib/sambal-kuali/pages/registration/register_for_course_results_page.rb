class RegisterForCourseResults < RegisterForCourseBase
  page_url "#{$test_site}/registration/index.jsp#/search"

  element(:results_table) { |b| b.table(id: "search_results_table") }

  # Table column indexes
  COURSE_CODE = 0
  COURSE_DESC = 1
  COURSE_CRED = 2

  def target_result_row_by_course(course_code)
    results_table.rows.each do |row|
      return row if row.cells[COURSE_CODE].text.match course_code
    end
    return nil
  end

  def target_result_row_by_index(ind)
    results_table.rows[ind]
  end

  def course_desc_link(course_code)
    row = target_result_row_by_course(course_code)
    row.cells[COURSE_DESC].link
  end

  def course_code_by_index(index)
    row = target_result_row_by_index (index)
    row.cells[COURSE_CODE].text
  end

  def results_course_title_by_index (index)
    item = target_result_row_by_index (index)
    row.cells[COURSE_DESC].text
  end

  def results_credits_by_index (index)
    item = target_result_row_by_index (index)
    row.cells[COURSE_CRED].text
  end

end