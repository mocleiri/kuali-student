class ManageRegistrationGroups < BasePage

  wrapper_elements
  frame_element

  expected_element :subject_code

  element(:subject_code) { |b| b.frm.div(id: "manageRegistrationGroupsView").h3.span() } # Persistent ID needed!
  element(:format_select) { |b| b.frm.div(data_label: "Select Format").select() }

  element(:page_validation_error_list) { |b| b.frm.ul(id: "pageValidationList") }
  value(:first_page_validation_error)  { |b| b.page_validation_error_list.li().text() }

  element(:unassigned_ao_table) { |b| b.frm.div(id: "KS-ManageRegistrationGroups-UnassignedActivityOfferingsPerFormatSection").table() }

  def target_unassigned_ao_row(ao_code)
    unassigned_ao_table.row(text: /\b#{ao_code}\b/)
  end

  def select_unassigned_ao_row(ao_code)
    target_unassigned_ao_row(ao_code).cells[0].checkbox().set
  end

  def unassigned_ao_list
    codes = []
    if unassigned_ao_table.exists?
      unassigned_ao_table.rows.each { |row| codes << row[get_unassigned_ao_code_column_index()].text }
      codes.delete_if { |code| code == "CODE" }
      codes.delete_if { |code| code.strip == "" }
    end
    codes
  end

  def get_unassigned_ao_code_column_index
    column = 1
    if unassigned_ao_table.rows[0].cells[0].text() == "CODE"
      column = 0
    end
    return column
  end

  action(:generate_unconstrained_reg_groups) { |b| b.frm.button(id: "generate_unconstrained_rgs_button").click; b.loading.wait_while_present}
  element(:ao_cluster_select) { |b| b.frm.select(id: "KS-ManageRegistrationGroups-ClusterForFormat_Dropdown_control") }
  action(:ao_cluster_assign_button) { |b| b.frm.button(id: "move_ao_button").click; b.loading.wait_while_present}

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
  value(:create_cluster_first_error_msg)  { |b| b.div(id: /jquerybubblepopup/).ul.li.text() }
  #end create cluster dialog

  #delete cluster dialog
  element(:deleteClusterDialog_div)  { |b| b.frm.div(id: "confirmToDeleteClusterDialog") }
  action(:confirm_delete_cluster){ |b|b.deleteClusterDialog_div.checkbox(index: 0).click; b.loading.wait_while_present}
  action(:cancel_delete_cluster){ |b|b.deleteClusterDialog_div.checkbox(index: 1).click; b.loading.wait_while_present}
  #end create cluster dialog

  def cluster_div_list
    div_list = []
    if cluster_list_div.exists?
      div_list = cluster_list_div.divs(class: "uif-group uif-boxGroup uif-horizontalBoxGroup uif-collectionItem uif-boxCollectionItem")
    end
    div_list
  end

  def delete_all_clusters
    cluster_div_list.to_a.reverse.each do |div|
      div.link(text: "Delete").click
      loading.wait_while_present
      confirm_delete_cluster
    end
  end

  def cluster_list_item_div(private_name)
    img_id = cluster_list_div.span(text: /#{Regexp.escape("#{private_name}")}/).image().id
    div_id = img_id[0..-5]    #eg changes  u532_line0_exp to u532_line0
    cluster_list_div.div(id: "#{div_id}")
  end

  def cluster_name_text(private_name)
   cluster_list_item_div(private_name).span().text()
  end

  def cluster_generate_reg_groups(private_name)
    cluster_list_item_div(private_name).link(text: "Generate Registration Groups").click
    loading.wait_while_present
  end

  def rename_cluster(private_name)
    cluster_list_item_div(private_name).link(text: "Rename").click
  end

  def remove_cluster(private_name)
    cluster_list_item_div(private_name).link(text: "Delete").click
  end

  def get_cluster_status_msg(private_name)
    cluster_list_item_div(private_name).div(id: /KS-ManageRegistrationGroups-StateAndActionLinks_line/).span.text()
  end

  def get_cluster_first_error_msg(private_name)
    cluster_list_item_div(private_name).li(class: "uif-errorMessageItem").text()
  end

  def get_cluster_first_warning_msg(private_name)
    cluster_list_item_div(private_name).li(class: "uif-warningMessageItem").text()
  end

  def get_cluster_ao_row(private_name, ao_code)
    cluster_list_item_div(private_name).table.row(text: /\b#{Regexp.escape(ao_code)}\b/)
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