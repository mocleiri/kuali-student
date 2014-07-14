class AdminRegistration < BasePage

  expected_element :cancel_update_link

  wrapper_elements
  frame_element
  element(:admin_registration_page) { |b| b.frm.div(id: "KS-AdminRegistration")}
  element(:student_info_section) { |b| b.frm.div(id: "KS-AdminRegistration-StudentInfo")}
  element(:student_info_input) { |b| b.student_info_section.text_field(id: /studentIdField/)}
  element(:student_info_go_btn) { |b| b.student_info_section.button(id: "go_button")}
  action(:student_info_go){ |b| b.student_info_go_btn.when_present.click}
  element(:student_info_msg) { |b| b.student_info_section.div(id: /studentInfoMsg/)}
  value(:student_info_msg_value){ |b| b.student_info_msg.when_present.text}
end