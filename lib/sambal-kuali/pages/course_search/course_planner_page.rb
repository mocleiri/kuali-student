class CoursePlannerPage < BasePage

  page_url = "#{$test_site}myplan/course?methodToCall=start&viewId=CourseSearch-FormView"

  wrapper_elements
  frame_element

  #current_term_add
  action(:current_term_add) { |b| b.a(id: "u297_line8").click }

  #future_term_add
  action(:future_term_add) { |b| b.a(id: "u297_line15").click }

  #subject
  element(:course_code_text) { |b| b.frm.text_field(id: "u87_control") }

  #credit
  element(:credit) { |b| b.input(id: "u95_control") }

  #notes
  element(:notes) { |b| b.frm.text_field(id: "u103_control") }

  #add_to_plan button
  action(:add_to_plan) { |b| b.frm.button(id: "u35").click }

  #cancel
  action(:cancel) { |b| b.frm.button(id: "u36").click }

  #course_added_to_current_term
  action(:course_added_to_current_term) { |b| b.div(id:"u357_line8_line0_boxLayout" ).click}





end