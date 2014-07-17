class AdminRegistration < BasePage

  #expected_element :cancel_update_link

  wrapper_elements
  frame_element
  validation_elements

  element(:admin_registration_page) { |b| b.frm.div(id: "KS-AdminRegistration")}
  element(:student_info_section) { |b| b.frm.div(id: "KS-AdminRegistration-StudentInfo")}
  element(:student_info_input) { |b| b.student_info_section.text_field(id: /studentIdField/)}
  element(:student_info_go_btn) { |b| b.student_info_section.button(id: "go_button")}
  action(:student_info_go){ |b| b.student_info_go_btn.when_present.click}
  element(:student_info_msg) { |b| b.student_info_section.div(id: /studentInfoMsg/)}
  value(:student_info_msg_value){ |b| b.student_info_msg.when_present.text}
  element(:student_error_message) { |b| b.student_info_section.li(class: 'uif-errorMessageItem')}
  element(:registration_change_term_section) { |b| b.frm.div(id: "KS-AdminRegistration-ChangeTermSection")}
  element(:change_term_input) { |b| b.registration_change_term_section.text_field(id: /termCodeField/)}
  element(:change_term_go_btn) { |b| b.registration_change_term_section.button(id: /changeTermGoButton/)}
  action(:change_term_go){ |b| b.change_term_go_btn.when_present.click}
  element(:change_term_info_msg) { |b| b.registration_change_term_section.div(id: "changeTermInfoMsg")}
  value(:change_term_info_msg_value){ |b| b.change_term_info_msg.when_present.text}
  element(:change_term_error_message) { |b| b.registration_change_term_section.li(class: 'uif-errorMessageItem')}

  def get_student_error_message
    loading.wait_while_present
    student_error_message.text
  end

  def get_term_error_message
    loading.wait_while_present
    change_term_error_message.text
  end

  def get_change_term_info_message
    loading.wait_while_present
    change_term_info_msg.text
  end

  #################################################################
  ### Register Courses Table
  #################################################################
  element(:registered_courses_section) { |b| b.frm.section( id: "KS-AdminRegistration-Registered")}
  element(:registered_courses_table) { |b| b.registered_courses_section.table}
  element(:registered_courses_header) { |b| b.registered_courses_section.text}

  COURSE_CODE = 0
  COURSE_NAME = 1
  CREDITS     = 2
  REG_OPTIONS = 3
  ACTIVITY    = 4
  DATE_TIME   = 5
  INSTRUCTOR  = 6
  ROOM        = 7
  REG_DATE    = 8

  def registered_courses_row
    loading.wait_while_present
    if registered_courses_table.exists?
    array = []
    registered_courses_table.rows().each do |row|
         array << row
    end
    return array
    else
      return nil
    end
  end

  def get_registered_course_credits(row)
    loading.wait_while_present
    row.cells[CREDITS].text
  end

  def get_registered_course_code_sort
    loading.wait_while_present
    registered_courses_table.th(class: "sorting_asc").text
  end

  #################################################################
  ### Wait listed Courses Table
  #################################################################

  element(:waitlisted_courses_section) { |b| b.frm.section( id: "KS-AdminRegistration-Waitlist")}
  element(:waitlisted_courses_table) { |b| b.waitlisted_courses_section.table}
  element(:waitlisted_courses_header) { |b| b.waitlisted_courses_section.text}

  def waitlisted_courses_row
    loading.wait_while_present
    if waitlisted_courses_table.exists?
      array = []
      waitlisted_courses_table.rows().each do |row|
        array << row
      end
      return array
    else
      return nil
    end
  end

  def get_waitlisted_course_credits(row)
    loading.wait_while_present
    row.cells[CREDITS].text
  end

  def get_waitlisted_course_code_sort
    loading.wait_while_present
    waitlisted_courses_table.th(class: "sorting_asc").text
  end
end