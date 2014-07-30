class AdminRegistration < BasePage

  expected_element :student_info_input

  wrapper_elements
  frame_element
  validation_elements

  COURSE_CODE = 0
  COURSE_NAME = 1
  SECTION = 1
  MULTI_COURSE_CODES = 1
  POPUP_CREDITS = 1
  CREDITS     = 2
  MULTI_SECTIONS = 2
  POPUP_REG_OPTIONS = 2
  POPUP_REG_EFFECTIVE_DATE = 3
  REG_OPTIONS = 3
  ACTIVITY    = 4
  DATE_TIME   = 5
  INSTRUCTOR  = 6
  ROOM        = 7
  REG_DATE    = 8

  element(:admin_registration_page) { |b| b.frm.div(id: "KS-AdminRegistration")}
  element(:effective_date_float_table) { |b| b.div(id: /jquerybubblepopup/).table}
  element(:effective_date_float_icon) { |row, b| b.loading.wait_while_present; row.span(class: "icon-notification")}

  def get_effective_date_float date
    effective_date_float_table.rows(text: /#{date}/).each do |row|
      return row.text
    end
    return nil
  end

  #################################################################
  ### Student and Term
  #################################################################
  element(:student_info_section) { |b| b.frm.div(id: "KS-AdminRegistration-StudentInfo")}
  element(:student_info_input) { |b| b.loading.wait_while_present; b.student_info_section.text_field(id: /studentIdField/)}
  element(:student_info_go_btn) { |b| b.student_info_section.button(id: "go_button")}
  action(:student_info_go){ |b| b.student_info_go_btn.when_present.click}

  element(:student_info_msg) { |b| b.student_info_section.div(id: /studentInfoMsg/)}
  value(:student_info_msg_value){ |b| b.student_info_msg.when_present.text}
  element(:student_error_message) { |b| b.student_info_section.li(class: 'uif-errorMessageItem')}
  value(:get_student_error_message){ |b| b.student_error_message.when_present.text}

  element(:registration_change_term_section) { |b| b.frm.div(id: "KS-AdminRegistration-ChangeTermSection")}
  element(:change_term_input) { |b| b.loading.wait_while_present; b.registration_change_term_section.text_field(id: /termCodeField/)}
  element(:change_term_go_btn) { |b| b.registration_change_term_section.button(id: /changeTermGoButton/)}
  action(:change_term_go){ |b| b.change_term_go_btn.when_present.click}

  element(:change_term_info_msg) { |b| b.registration_change_term_section.div(id: "changeTermInfoMsg")}
  value(:get_change_term_info_message){ |b| b.change_term_info_msg.when_present.text}
  element(:change_term_error_message) { |b| b.registration_change_term_section.li(class: 'uif-errorMessageItem')}
  value(:get_term_error_message){ |b| b.change_term_error_message.when_present.text}

  element(:confirm_term_popup_section) { |b| b.frm.section(id: "termEligibilityDialog")}
  element(:confirm_term_dialog_table) { |b| b.frm.div(id: "KS-AdminRegistration-TermIssues").table}
  element(:confirm_term_continue_btn) { |b| b.confirm_term_popup_section.button(id: "termContinue_btn")}
  action(:confirm_term_continue){ |b| b.confirm_term_continue_btn.when_present.click}
  element(:confirm_term_cancel_link) { |b| b.confirm_term_popup_section.a(id: "cancelTerm_link")}
  action(:confirm_term_cancel){ |b| b.confirm_term_cancel_link.when_present.click}

  #################################################################
  ### Course Code and Section
  #################################################################
  element(:admin_registration_reg_for_section) { |b| b.frm.div(id: "KS-AdminRegistration-RegFor")}
  element(:admin_registration_reg_for_table) { |b| b.admin_registration_reg_for_section.table}
  element(:course_code_input) { |b| b.get_blank_row("code")}
  element(:section_code_input) { |b| b.get_blank_row("section")}
  element(:course_description_message) { |b| b.admin_registration_reg_for_section.div(class: "uif-messageField")}

  element(:reg_for_error_message) { |b| b.frm.div(id: "KS-AdminRegistration-RegFor_messages")}

  element(:course_addline_btn) { |b| b.admin_registration_reg_for_section.button(id: "addLineButton")}
  action(:course_addline){ |b| b.course_addline_btn.when_present.click}
  element(:course_register_btn) { |b| b.admin_registration_reg_for_section.button(id: "KS-AdminRegistration-RegisterButton")}
  action(:course_register){ |b| b.course_register_btn.when_present.click}
  element(:course_delete_btn) { |index, b| b.admin_registration_reg_for_section.button(id: /KS-AdminRegistration-RegFor_del_line#{index}/) }
  action(:course_delete) { |index, b| b.course_delete_btn(index).when_present.click}
  element(:cancelled_section_error_message) { |b| b.reg_for_error_message.li(class: 'uif-errorMessageItem')}
  value(:get_cancelled_section_error_message){ |b| b.cancelled_section_error_message.when_present.text}

  element(:confirm_registration_popup_section) { |b| b.frm.section(id: "registerConfirmDialog")}
  element(:confirm_registration_popup_table) { |b| b.frm.div(id: "KS-AdminRegistration-DialogCollection").table}
  element(:confirm_registration_btn) { |b| b.confirm_registration_popup_section.button(id: "confirmRegistrationButton")}
  action(:confirm_registration){ |b| b.confirm_registration_btn.when_present.click}
  element(:cancel_registration_link) { |b| b.confirm_registration_popup_section.a(id: "cancelRegistrationLink")}
  action(:cancel_registration){ |b| b.cancel_registration_link.when_present.click}
  value(:get_course_description_message){ |b| b.loading.wait_while_present; b.course_description_message.when_present.text}

  element(:admin_registration_issues_section) { |b| b.frm.div(id: "KS-AdminRegistration-Results").table}
  element(:confirm_registration_issue_btn) { |b| b.admin_registration_issues_section.button(id: "u1o0zn0m_line0")}
  action(:confirm_registration_issue){ |b| b.confirm_registration_issue_btn.when_present.click}

  element(:get_registration_results_success) { |b| b.admin_registration_issues_section.ul(class: 'uif-readOnlyStringList').when_present.text}
  element(:registration_results_btn) { |b| b.admin_registration_issues_section.button(id: "KS-AdminRegistration-Results_del_line0")}
  action(:dismiss_registration_result){ |b| b.registration_results_btn.when_present.click}

  def get_blank_row(cell_type)
    loading.wait_while_present
    admin_registration_reg_for_table.rows[1..-1].each do |row|
      row.text_fields.each do |input|
        if input.text == "" and input.attribute_value('name') =~ /#{Regexp.escape(cell_type)}/
          return input if input.attribute_value('value') == ""
        end
      end
    end
    return nil
  end

  def get_confirm_registration_row(text)
    confirm_registration_popup_table.rows[1..-1].each do |row|
      return row if row.text =~ /#{Regexp.escape(text)}/
    end
  end

  def get_last_course_code_value
    if admin_registration_reg_for_table.rows.length > 1
      admin_registration_reg_for_table.rows[-1].cells[MULTI_COURSE_CODES].text_field().value
    else
      admin_registration_reg_for_table.rows[-1].cells[COURSE_CODE].text_field().value
    end
  end

  def get_last_section_value
    if admin_registration_reg_for_table.rows.length > 1
      admin_registration_reg_for_table.rows[-1].cells[MULTI_SECTIONS].text_field().value
    else
      admin_registration_reg_for_table.rows[-1].cells[SECTION].text_field().value
    end
  end

  def get_course_code_value text
    rows = admin_registration_reg_for_table.rows(text: /#{text}/)
    rows.each do |row|
      if rows.length > 1
        return row.cells[MULTI_COURSE_CODES].text_field().value
      else
        return row.cells[COURSE_CODE].text_field().value
      end
    end
    return nil
  end

  def get_section_value text
    rows = admin_registration_reg_for_table.rows(text: /#{text}/)
    rows.each do |row|
      if rows.length > 1
        return row.cells[MULTI_SECTIONS].text_field().value
      else
        return row.cells[SECTION].text_field().value
      end
    end
    return nil
  end

  #################################################################
  ### Register Courses Table
  #################################################################
  element(:registered_courses_section) { |b| b.frm.section( id: "KS-AdminRegistration-Registered")}
  element(:registered_courses_table) { |b| b.registered_courses_section.table}
  value(:registered_courses_header) { |b| b.registered_courses_section.text}
  value(:get_registered_course_credits){ |row, b| b.loading.wait_while_present; row.cells[CREDITS].text}
  value(:get_registered_course_reg_date){ |row, b| b.loading.wait_while_present; row.cells[REG_DATE].text}
  value(:get_registered_course_code_sort){ |b| b.loading.wait_while_present; b.registered_courses_table.th(class: "sorting_asc").text}

  def registered_courses_rows
    array = []

    loading.wait_while_present
    if registered_courses_table.exists?
      registered_courses_table.rows().each do |row|
        array << row
      end
    end

    return array
  end

  def get_registered_course (course,section)
    registered_courses_table.rows(text: /#{course} (#{section})/).each do |row|
      return row.text
    end
    return nil
  end


  #################################################################
  ### Wait listed Courses Table
  #################################################################
  element(:waitlisted_courses_section) { |b| b.frm.section( id: "KS-AdminRegistration-Waitlist")}
  element(:waitlisted_courses_table) { |b| b.waitlisted_courses_section.table}
  value(:waitlisted_courses_header) { |b| b.waitlisted_courses_section.text}
  value(:get_waitlisted_course_credits){ |row, b| b.loading.wait_while_present; row.cells[CREDITS].text}
  value(:get_waitlisted_course_code_sort){ |b| b.waitlisted_courses_table.th(class: "sorting_asc").text}

  def waitlisted_courses_rows
    array = []

    loading.wait_while_present
    if waitlisted_courses_table.exists?
      waitlisted_courses_table.rows().each do |row|
        array << row
      end
    end

    return array
  end

  #################################################################
  ### Default Popup Options
  #################################################################
  value(:get_course_default_credits){ |code, b| b.loading.wait_while_present; b.get_confirm_reg_dialog_row(code).cells[POPUP_CREDITS].text}
  value(:get_course_default_reg_options){ |code, b| b.loading.wait_while_present; b.get_confirm_reg_dialog_row(code).cells[POPUP_REG_OPTIONS].text}
  value(:get_course_default_effective_date){ |code, b| b.loading.wait_while_present; b.get_confirm_reg_dialog_row(code).cells[POPUP_REG_EFFECTIVE_DATE].text_field().value}
  element(:set_course_default_effective_date){ |code, b| b.loading.wait_while_present; b.get_confirm_reg_dialog_row(code).cells[POPUP_REG_EFFECTIVE_DATE].text_field()}


  def get_confirm_reg_dialog_row(text)
    loading.wait_while_present
    if confirm_registration_popup_table.exists?
      confirm_registration_popup_table.rows[1..-1].each do |row|
        return row if row.text =~ /#{text}/
      end
    end
  end
end