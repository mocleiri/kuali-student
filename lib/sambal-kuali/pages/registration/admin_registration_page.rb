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
  element(:registration_change_term_section) { |b| b.frm.div(id: "registrationChangeTermSection")}
  element(:change_term_input) { |b| b.registration_change_term_section.text_field(id: /termCodeField/)}
  element(:change_term_go_btn) { |b| b.registration_change_term_section.button(id: "changeTermGoButton")}
  action(:change_term_go){ |b| b.change_term_go_btn.when_present.click}
  element(:change_term_info_msg) { |b| b.registration_change_term_section.div(id: /studentInfoMsg/)}
  value(:change_term_info_msg_value){ |b| b.change_term_info_msg.when_present.text}


  def student_error_message; student_info_section.li(class: 'uif-errorMessageItem'); end



  def get_student_error_message
    loading.wait_while_present
    student_error_message.text
  end

end