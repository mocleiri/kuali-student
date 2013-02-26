class ActivityOfferingLogistics < BasePage
  wrapper_elements
  frame_element

  action(:save_request) { |b| b.frm.button(text: "Save Request").click; b.loading.wait_while_present }
  element(:cancel_btn_element) { |b| b.frm.link(text: "Cancel");}
  action(:cancel) { |b| b.frm.link(text: "Cancel").click; b.loading.wait_while_present }
  action(:save_and_process) { |b| b.frm.button(text: "Save and Process Request").click; b.loading.wait_while_present }

end