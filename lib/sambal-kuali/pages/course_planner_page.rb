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

  #elements for edit plan item
  #action(:course_code_current_term_edit_plan_item_click) { |b| b.td(class: "jquerybubblepopup-innerHtml").div(id: "u504_boxLayout").a(:id => /planner_menu_edit_plan_item*/).click }
  action(:course_code_current_term_edit_plan_item_click) { |b| b.td(class: "jquerybubblepopup-innerHtml").a(:id => /planner_menu_edit_plan_item*/).click }
  action(:course_code_current_term_click) { |b| b.span(id:"u541_span" ).click }

  action(:edit_plan_item_click) { |b| b.course_code_current_term.click; b.edit_plan_item.click }

  #action(:edit_plan_item_action) { |b| b.a(id: "uif-actionLink uif-boxLayoutVerticalItem clearfix").a(title: "Edit plan item").click }
  #element(:edit_plan_item) { |b| b.link(:id => /planner_menu_edit_plan_item*/) }
  #action(:edit_plan_item_action) { |b| b.edit_plan_item_current_term.click }
  #elements to be used for verifying added course


  #element(:view_cterm_course_planner_notes) { |b| b.td(class: "jquerypopover-innerHtml").textarea(id: "u95_control").text }
  element(:view_cterm_course_planner_notes) { |b| b.textarea(id: "u95_control").text }
  #element(:view_cterm_course_planner_notes) { |b| b.frm.text_field(id: "u95_control") }


end

