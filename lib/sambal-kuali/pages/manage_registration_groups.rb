class ManageRegistrationGroups < BasePage

  wrapper_elements
  frame_element

  expected_element :subject_code

  element(:subject_code) { |b| b.frm.div(id: "manageRegistrationGroupsView").h3.span() } # Persistent ID needed!
  element(:format_select) { |b| b.frm.div(data_label: "Select Format").select() }

  element(:unassigned_ao_table) { |b| b.frm.div(id: "KS-ManageRegistrationGroups-UnassignedActivityOfferingsPerFormatSection").table() }

  def target_unassigned_ao_row(ao_code)
    unassigned_ao_table.row(text: /\b#{ao_code}\b/)
  end

  def select_unassigned_ao_row(ao_code)
    target_unassigned_ao_row(ao_code).cells[0].checkbox().set
  end

  action(:generate_unconstrained_reg_groups) { |b| b.frm.button(id: "generate_unconstrained_rgs_button").click b.loading.wait_while_present}
  element(:ao_cluster_select) { |b| b.frm.select(id: "KS-ManageRegistrationGroups-ClusterForFormat_Dropdown_control") }
  action(:ao_cluster_assign_button) { |b| b.frm.button(id: "move_ao_button").click b.loading.wait_while_present}

  action(:create_new_cluster){ |b|b.frm.button(id: /create_new_cluster_button/).click; b.loading.wait_while_present}

  action(:add_button) { |b| b.frm.div(id: "createNewClusterSection").button(text: "Create Cluster").click; b.loading.wait_while_present }

  action(:generate_reg_groups_button) { |b| b.frm.button(id: "generate_unconstrained_rgs_button").click; b.loading.wait_while_present }

  element(:cluster_list_div)  { |b| b.frm.div(id: "KS-ManageRegistrationGroups-ClusterCollection") }

  #create cluster dialog
  element(:createNewClusterDialog_div)  { |b| b.frm.div(id: "createNewClusterDialog") }
  element(:private_name) { |b| b.createNewClusterDialog_div.div(data_label: "Private Name").text_field() }
  element(:published_name) { |b| b.createNewClusterDialog_div.div(data_label: "Published Name").text_field() }
  action(:create_cluster){ |b|b.createNewClusterDialog_div.checkbox(index: 0).click; b.loading.wait_while_present}
  action(:cancel_create_cluster){ |b|b.createNewClusterDialog_div.checkbox(index: 1).click; b.loading.wait_while_present}
  #end create cluster dialog

  def cluster_list_item_div_id(private_name)
    img_id = cluster_list_div.span(text: /#{Regexp.escape("#{private_name}")}/).image().id
    puts "img_id: #{img_id}"
    #img_id = cluster_list_div.span(text: /^#{Regexp.escape("#{private_name}")}\s/).image().id
    img_id[0..-5]    #eg changes  u532_line0_exp to u532_line0
  end

  def cluster_list_name_text(private_name)
   div_id = cluster_list_item_div_id(private_name)
   cluster_list_div.div(id: "#{div_id}").span().text()
  end

  def cluster_list_generate_reg_groups(private_name)
    div_id = cluster_list_item_div_id(private_name)
    cluster_list_div.div(id: "#{div_id}").link(text: "Generate Registration Groups").click
  end

  def cluster_list_row_rename_cluster(private_name)
    div_id = cluster_list_item_div_id(private_name)
    cluster_list_div.div(id: "#{div_id}").link(text: "Rename").click
  end

  def cluster_list_row_remove_cluster(private_name)
    div_id = cluster_list_item_div_id(private_name)
    cluster_list_div.div(id: "#{div_id}").link(text: "Delete").click
  end

  def get_cluster_ao_row(private_name, ao_code)
    div_id = cluster_list_item_div_id(private_name)
    cluster_list_div.div(id: "#{div_id}").table.row(text: /\b#{Regexp.escape(ao_code)}\b/)
  end

  #test script shell
  #@course_offering = make CourseOffering, :course=>"ENGL103"
  #@course_offering.manage
  #on ManageCourseOfferings do |page|
  #  page.manage_registration_groups
  #end
  #on ManageRegistrationGroups do |page|
  #  puts page.subject_code.text()
  #  #page.private_name.set "test1pri"
  #  #page.published_name.set "test1pub"
  #  #
  #  #puts page.ao_table.rows.count
  #  puts page.cluster_list_row_name_text("test1pri")
  #  # page.ao_cluster_select.select("test1")
  #  #page.cluster_list_row_generate_reg_groups("test1")
  #  puts page.target_ao_row("A").cells[1].text
  #  puts page.target_ao_row("A").cells[2].text
  #  page.select_ao_row("A")
  #  page.ao_cluster_select.select("test1pub")
  #  page.ao_cluster_assign_button
  #end


end