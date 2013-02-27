class ManageCourseOfferings < BasePage

  expected_title /Kuali :: Manage Course Offering/

  wrapper_elements
  frame_element

  expected_element :term

  element(:error_message_course_not_found) { |b| b.frm.li(class: "uif-errorMessageItem") }

  element(:term) { |b| b.frm.text_field(name: "termCode") }
  element(:course_offering_code) { |b| b.frm.radio(value: "courseOfferingCode") }
  element(:subject_code) { |b| b.frm.radio(value: "subjectCode") }
  element(:input_code) { |b| b.frm.text_field(name: "inputCode") }

  element(:manage_offering_links_div) { |b| b.frm.div(id: "KS-CourseOfferingManagement-CourseOfferingLinks")}
  action(:delete_offering) { |b| b.manage_offering_links_div.link(id: "ActivityOfferingResultSection-deleteOneCoWithLink").click; b.loading.wait_while_present }
  action(:manage_registration_groups) { |b| b.manage_offering_links_div.link(text: "Manage Registration Groups").click }

  action(:show) { |b| b.frm.button(text: "Show").click; sleep 2; b.loading.wait_while_present(180) }

  value(:course_title) { |b| b.frm.div(id: "ActivityOfferingResultSection").text }
  element(:edit_offering_element) { |b| b.frm.link(text: "Edit")}
  action(:edit_offering) { |b| b.frm.link(text: "Edit").click; b.loading.wait_while_present(200) } # Persistent ID needed!

  element(:format) { |b| b.frm.select(name: "formatIdForNewAO") }
  element(:activity_type) { |b| b.frm.select(name: "activityIdForNewAO") }
  element(:quantity) { |b| b.frm.text_field(name: "noOfActivityOfferings") }
  element(:create_co_button)   { |b| b.frm.button(id: "KS-CourseOfferingManagement-ToolBar-Add-CO") }

  action(:add) { |b| b.frm.button(id: "KS-CourseOfferingManagement-ToolBar-Add-AO").click; b.loading.wait_while_present }
  action(:delete_aos) { |b| b.frm.button(id: "KS-CourseOfferingManagement-ToolBar-Delete-AO").click; b.loading.wait_while_present }

  action(:delete_cos) { b|b.frm.button(id: "KS-CourseOfferingManagement-ToolBar-Delete-CO").click; b.loading.wait_while_present }
  
  action(:select_all) { |b| b.frm.link(id: "KS-CourseOfferingManagement-SelectAll").click; b.loading.wait_while_present }

  action(:add_course){ |b| b.frm.button(id: "KS-CourseOfferingManagement-ToolBar-Add-CO").click; b.loading.wait_while_present}

  action(:approve_cos){ |b| b.frm.button(id: "KS-CourseOfferingManagement-ToolBar-Approve-CO").click; b.loading.wait_while_present}

  element(:approve_co_popup_div) { |b| b.div(id: "approveCODialog") }
  action(:approve_co_confirm_action) { |b| b.approve_co_popup_div.checkbox(index: 0).click; b.loading.wait_while_present }
  action(:approve_co_cancel_action) { |b| b.approve_co_popup_div.checkbox(index: 1).click; b.loading.wait_while_present }

  action(:add_activity){ |b| b.frm.button(id: "KS-CourseOfferingManagement-ToolBar-Add-AO").click; b.loading.wait_while_present}
  action(:draft_activity){ |b| b.frm.button(id: "KS-CourseOfferingManagement-ToolBar-Draft-AO").click; b.loading.wait_while_present}
  action(:approve_activity){ |b| b.frm.button(id: "KS-CourseOfferingManagement-ToolBar-Approve-AO").click; b.loading.wait_while_present}

  AO_CODE = 1
  AO_STATUS = 2
  AO_TYPE = 3
  AO_FORMAT = 4
  AO_INSTRUCTOR = 5
  AO_MAX_ENR = 6

  element(:activity_offering_results_table) { |b| b.frm.table(class: "uif-tableCollectionLayout dataTable") } # Persistent ID needed!
  element(:selected_offering_actions) { |b| b.frm.select(name: "selectedOfferingAction") }

  action(:go) { |b| b.frm.button(text: "Go").click; b.loading.wait_while_present }

  def view_activity_offering(code)
    activity_offering_results_table.link(text: code).click
    loading.wait_while_present
  end

  def target_row(code)
#    activity_offering_results_table.row(text: /\b#{Regexp.escape(code)}\b/).wait_until_present(60)
    activity_offering_results_table.row(text: /\b#{Regexp.escape(code)}\b/)
  end

  def course_list_returned?(subject_code)
    activity_offering_results_table.row(text: /#{subject_code}/).exists?
  end

  def ao_db_id(code)
    target_row(code).cells[AO_CODE].link.attribute_value("href").scan(/aoInfo.id=(.*)&dataObjectClassName/)[0][0]
  end

  def ao_status(code)
    target_row(code).cells[AO_STATUS].text
  end

  def copy(code)
    target_row(code).link(text: "Copy").click
    loading.wait_while_present
  end

  def edit(code)
    target_row(code).link(text: "Edit").click
    loading.wait_while_present(120)
  end

  def select_aos(code_list)
    for code in code_list
      if !target_row(code).nil?
        target_row(code).checkbox.set
      end
    end
  end

  def ao_status(code, status)
    retVal = false
    row_text = target_row(code).text

    if row_text.include? status
      retVal = true
    end
    retVal
  end

  def ao_schedule_data(aoCode)
     retVal = nil
     retVal = target_row(aoCode).text
  end

  def codes_list
    codes = []
    activity_offering_results_table.rows.each { |row| codes << row[AO_CODE].text }
    codes.delete_if { |code| code == "CODE" }
    codes.delete_if { |code| code.strip == "" }
    codes
  end

  def add_ao input_format, input_quantity
    format.select(input_format)
    loading.wait_while_present(120)
    activity_type.select(activity_type.options[1].text)
    quantity.set(input_quantity)
    add
  end

  def cross_listed_as(crossListedCoCode)

    course_title
  end

  def check_all_ao_status(aoStatus)
    retVal = true
    activity_offering_results_table.rows.each {|row|
      if !(row[AO_STATUS].text.eql? aoStatus)
        retVal = false
        break
      end
    }
    retVal
  end

  def click_toolbar_button(button)
      case(button)
        when 'Add Activity Offering'
          add_activity
        when 'Approve Activity Offering'
          approve_activity
        when 'Set as Draft'
          draft_activity
        when 'Delete Activity Offering'
          delete_aos
        when 'Add Course'
          add_course
        when 'Approve'
          approve_cos
        when 'Delete'
          delete_cos
        else
          raise "undefined button."
      end
  end

  def check_aos_status(aoStatus, aos)
    retVal = true
    aos.each { |code|
    if(!ao_status(code, aoStatus))
      retVal = false
      break
    end
    }
    retVal
  end

end