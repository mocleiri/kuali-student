class CoursePlannerPage < BasePage

  page_url = "#{$test_site}myplan/course?methodToCall=start&viewId=CourseSearch-FormView"

  wrapper_elements
  frame_element

  #ELEMENTS ADD COURSE POPUP

  element(:course_code_text) { |b| b.frm.text_field(id: "u87_control") }
  element(:credit) { |b| b.frm.text_field(id: "u95_control") }
  element(:notes) { |b| b.frm.text_field(id: "u103_control") }
  element(:course_code_current_term_credit) { |b| b.span(id: "u544_span").text }
  element(:view_notes_popover) { |b| b.textarea(id: "u95_control").text }
  element(:view_variable_credit_popover) { |b| b.input(id: "u87_control").value }

  action(:add_to_plan) { |b| b.frm.button(id: "u35").click }
  action(:cancel) { |b| b.frm.button(id: "u36").click }
  action(:current_term_add) { |b| b.a(id: "u297_line8").click }
  action(:future_term_add) { |b| b.a(id: "u297_line14").click }
  action(:course_code_current_term_edit_plan_item_click) { |b| b.td(class: "jquerybubblepopup-innerHtml").a(:id => /planner_menu_edit_plan_item*/).click }
  action(:course_code_current_term_click) { |b| b.span(id:"u541_span" ).click }
  action(:edit_plan_item_click) { |b| b.course_code_current_term.click; b.edit_plan_item.click }
  action(:course_added_to_current_term) { |b| b.div(id:"u357_line8_line0_boxLayout" ).click}
  action (:edit_plan_popover_cancel) { |b| b.frm.button(id: "u36").click}
  action(:course_code_current_term_delete_click) { |b| b.td(class: "jquerybubblepopup-innerHtml").a(:id => /planner_menu_delete_plan_item*/).click }
  action(:delete_confirmation) { |b| b.div(id: "u33_boxLayout").button(id: "u35").click }

end

