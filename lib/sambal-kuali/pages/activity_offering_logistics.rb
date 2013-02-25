class ActivityOfferingLogistics < BasePage

  action(:save_request) { |b| b.frm.button(text: "Save Request").click; b.loading.wait_while_present }
  action(:cancel) { |b| b.frm.link(text: "Cancel").click; b.loading.wait_while_present }
  action(:save_and_process) { |b| b.frm.button(text: "Save and Process Request").click; b.loading.wait_while_present }

end