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

  #course code and section code
  element(:admin_registration_reg_for_section) { |b| b.frm.div(id: "KS-AdminRegistration-RegFor")}
  element(:admin_registration_reg_for_table) { |b| b.admin_registration_reg_for_section.table}
  element(:course_code_input) { |b| b.get_blank_row("code")}
  element(:section_code_input) { |b| b.get_blank_row("section")}
  element(:course_description_message) { |b| b.admin_registration_reg_for_section.div(class: "uif-messageField")}
  element(:reg_for_error_message) { |b| b.frm.div(id: "KS-AdminRegistration-RegFor_messages")}

  element(:course_add_btn) { |b| b.admin_registration_reg_for_section.button(text: /\+/)}
  action(:course_add){ |b| b.course_add_btn.when_present.click}
  element(:course_register_btn) { |b| b.admin_registration_reg_for_section.button(text: /Register/)}
  action(:course_register){ |b| b.course_register_btn.when_present.click}

  #COURSE_CODE = 0
  SECTION = 1

  def get_course_description_message
    loading.wait_while_present
    course_description_message.text
  end

  def get_blank_row(cell_type)
    loading.wait_while_present
    admin_registration_reg_for_table.rows[1..-1].each do |row|
      if row.text == "" or row.text == nil
        row.text_fields.each do |input|
          if  input.attribute_value('name') =~ /#{Regexp.escape(cell_type)}/
            return input if input.attribute_value('value') == ""
          end
        end
      end
    end
    return nil
  end

end