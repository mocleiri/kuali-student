class ManageSocPage < BasePage

  expected_element :term_code

  wrapper_elements
  frame_element

  element(:message_element) {|b| b.frm.ul(id: "pageValidationList").li}
  element(:message_element_by_text) {|msg_text,b| b.frm.ul(id: "pageValidationList").li(text: /#{msg_text}/)}
  value(:message) {|b| b.message_element.text}
  element(:term_code)  { |b| b.frm.text_field(id: "socTermField_control") }
  element(:manage_course_offerings)  { |b| b.frm.a(id: "ManageSOCView-SchedulingDetails-ManageCOButton") }

  action(:go_action) { |b| b.frm.button(id: "socShowButton").click; sleep 2; b.loading.wait_while_present }

  element(:lock_button)  { |b| b.frm.button(id: "ManageSOCView-SchedulingDetails-LockSetButton") }
  def lock_action
    sleep 4
    lock_button.wait_until_present
    lock_button.click
    loading.wait_while_present
    sleep 4
  end

  element(:lock_popup_div) { |b| b.section(id: "lockConfirmDialog") }

  def lock_confirm_action
    sleep 4
    confirm_button = lock_popup_div.radio(id: /control_0/)
    confirm_button.wait_until_present
    confirm_button.click
    loading.wait_while_present
    sleep 4
    term_code.wait_until_present #synch to parent page
  end

  action(:lock_cancel_action) { |b| b.lock_popup_div.span(class: 'ui-button-text',text: 'Cancel').click; b.loading.wait_while_present }

  element(:send_to_scheduler_button)  { |b| b.frm.button(id: "ManageSOCView-SchedulingDetails-SendToSchedulerButton") }
  action(:send_to_scheduler_action) { |b| b.send_to_scheduler_button.click; b.loading.wait_while_present; sleep 1 }
  element(:schedule_popup_div) { |b| b.section(id: "massScheduleConfirmDialog") }

  def schedule_confirm_action
    sleep 4
    confirm_button = schedule_popup_div.radio(id: /control_0/)
    confirm_button.wait_until_present
    confirm_button.click
    loading.wait_while_present
    sleep 4
    term_code.wait_until_present #synch to parent page
  end
  action(:schedule_cancel_action) { |b| b.schedule_popup_div.span(class: 'ui-button-text',text: 'Cancel').click; b.loading.wait_while_present }

  element(:final_edit_button)  { |b| b.frm.button(id: "ManageSOCView-SchedulingDetails-FinalEditButton") }
  action(:final_edit_action) { |b| b.final_edit_button.click; b.loading.wait_while_present; sleep 1 }
  element(:final_edit_popup_div) { |b| b.section(id: "finalEditConfirmDialog") }

  def final_edit_confirm_action
    sleep 4
    confirm_button = final_edit_popup_div.radio(id: /control_0/)
    confirm_button.wait_until_present
    confirm_button.click
    loading.wait_while_present
    sleep 4
    term_code.wait_until_present #synch to parent page
  end

  action(:final_edit_cancel_action) { |b| b.final_edit_popup_div.span(class: 'ui-button-text',text: 'Cancel').click; b.loading.wait_while_present }

  element(:publish_button)  { |b| b.frm.button(id: "ManageSOCView-SchedulingDetails-PublishButton") }
  action(:publish_action) { |b| b.publish_button.click; b.loading.wait_while_present; sleep 1 }
  element(:publish_popup_div) { |b| b.section(id: "massPublishConfirmDialog") }

  def publish_confirm_action
    sleep 4
    confirm_button = publish_popup_div.radio(id: /control_0/)
    confirm_button.wait_until_present
    confirm_button.click
    loading.wait_while_present
    sleep 4
    term_code.wait_until_present #synch to parent page
  end

  action(:publish_cancel_action) { |b| b.publish_popup_div.span(class: 'ui-button-text',text: 'Cancel').click; b.loading.wait_while_present }

  element(:create_eos_button)  { |b| b.frm.button(id: 'ManageSOCView-CreateEOBulk-SchedulerButton') }
  action(:create_eos_action) { |b| b.create_eos_button.click; b.loading.wait_while_present; sleep 1 }
  element(:create_eos_confirm_popup_div) { |b| b.section(id: "examOfferingConfirmDialog") }

  def create_eos_confirm_action
    sleep 4
    confirm_button = create_eos_confirm_popup_div.radio(id: /control_0/)
    confirm_button.wait_until_present
    confirm_button.click
    loading.wait_while_present
    sleep 4
    term_code.wait_until_present #synch to parent page
  end
  action(:create_eos_cancel_action) { |b| b.create_eos_confirm_popup_div.span(class: 'ui-button-text',text: 'Cancel').click; b.loading.wait_while_present }

  element(:create_eos_error_popup_div) { |b| b.section(id: "errorNoExamPeriodDialog") }
  value(:create_eos_error_popup_msg) { |b| b.create_eos_error_popup_div.p(class: "uif-message").text }
  action(:eos_error_cancel_action) { |b| b.create_eos_error_popup_div.span(class: 'ui-button-text',text: 'Cancel').click; b.loading.wait_while_present }

  element(:eo_creation_status) { |b| b.div(id: 'eoSlottingStatus').text }
  element(:eo_creation_initiated_date) { |b| b.div(id: 'eoSlottingInitiatedDate').text }
  element(:eo_creation_completed_date) { |b| b.div(id: 'eoSlottingCompleteDate').text }
  element(:eo_creation_duration) { |b| b.div(id: 'eoSlottingDuration').text }

  element(:close_button)  { |b| b.frm.button(id: "ManageSOCView-SchedulingDetails-CloseButton") }
  element(:soc_status_table) { |b| b.div(id: "ManageSOCView-StatusHistory-SubSection2").table }
  STATE_COLUMN = 0
  EFFECTIVE_DATE_COLUMN = 1

  SOC_DRAFT = 'Draft'
  SOC_OPEN = 'Open'
  SOC_LOCKED = 'Locked'
  SOC_FINAL_EDITS = 'Final Edits'
  SOC_PUBLISHED = 'Published'
  SOC_CLOSED = 'Closed'

  def target_row soc_state
    soc_status_table.row(text: /#{Regexp.escape(soc_state)}/)
  end

  def soc_status
    last_completed_state = 'Draft'
    soc_status_table.rows[1..-1].each do |row|
      if row.cells[EFFECTIVE_DATE_COLUMN].text != ''
        last_completed_state = row.cells[STATE_COLUMN].text
      else
        break
      end
    end
    last_completed_state
  end

  element(:soc_scheduling_status) { |b| b.div(id: "socSchedulingStatus").text }
  element(:soc_publishing_status) { |b| b.div(id: "socPublishingStatus").text }
  element(:schedule_initiated_date) { |b| b.div(id: "schedule_initiated_date").text }
  element(:schedule_completed_date) { |b| b.div(id: "schedule_completed_date").text }
  element(:schedule_duration) { |b| b.div(id: "schedule_duration").text }
  element(:publish_initiated_date) { |b| b.div(id: "publish_initiated_date").text }
  element(:publish_completed_date) { |b| b.div(id: "publish_completed_date").text }
  element(:publish_duration) { |b| b.div(id: "publish_duration").text }


  element(:status_table) { |b| b.div(id: "ManageSOCView-StatusHistory-SubSection2").table }

  def is_date_exists(status_name)
    status_table.row(text: /\b#{Regexp.escape(status_name)}\b/).cells[1].text != nil
  end

end