#Student Eligibility & Prerequisite
When /^I copy and edit the Course Offering rule to the Student Eligibility & Prerequisite section$/ do
  #@activityOR = make AORequisitesData, :section => "Student Eligibility & Prerequisite"
  @prereq = make AOPreparationPrerequisiteRule, :term => "201208", :course => "ENGL304"
  @prereq.sepr_copy_edit_co_rule
end

When /^I copy the Course Offering rule to the Student Eligibility & Prerequisite section$/ do
  #@activityOR = make AORequisitesData, :section => "Student Eligibility & Prerequisite"
  @prereq = make AOPreparationPrerequisiteRule, :term => "201208", :course => "ENGL304"
  @prereq.sepr_copy_co_rule
end

When /^I revert the copied rule in the Student Eligibility & Prerequisite section$/ do
  #@activityOR = make AORequisitesData, :section => "Student Eligibility & Prerequisite"
  @prereq = make AOPreparationPrerequisiteRule, :term => "201208", :course => "ENGL304"
  @prereq.sepr_copy_co_rule
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.prereq_revert
  end
end

When /^I add a rule to the Student Eligibility & Prerequisite section$/ do
  #@activityOR = make AORequisitesData, :section => "Student Eligibility & Prerequisite"
  @prereq = make AOPreparationPrerequisiteRule, :term => "201301", :course => "HIST111"
  @prereq.sepr_add_ao_rule
end

When /^I delete a newly added rule in the Student Eligibility & Prerequisite section$/ do
  #@activityOR = make AORequisitesData, :section => "Student Eligibility & Prerequisite"
  @prereq = make AOPreparationPrerequisiteRule, :term => "201301", :course => "HIST111"
  @prereq.sepr_delete_added_ao_rule
end

When /^I delete the copied rule in the Student Eligibility & Prerequisite section$/ do
  #@activityOR = make AORequisitesData, :section => "Student Eligibility & Prerequisite"
  @prereq = make AOPreparationPrerequisiteRule, :term => "201208", :course => "ENGL304"
  @prereq.sepr_delete_copied_ao_rule
end

When /^I delete the copied and edited rule in the Student Eligibility & Prerequisite section$/ do
  #@activityOR = make AORequisitesData, :section => "Student Eligibility & Prerequisite"
  @prereq = make AOPreparationPrerequisiteRule, :term => "201208", :course => "ENGL304"
  @prereq.sepr_delete_copied_edited_ao_rule
end

When /^I view the catalog and course offering rule for the Student Eligibility & Prerequisite section$/ do
  #@activityOR = make AORequisitesData, :section => "Student Eligibility & Prerequisite"
  @prereq = make AOPreparationPrerequisiteRule, :term => "201208", :course => "ENGL304"
  @prereq.sepr_view_catalog_co_rule
end

When /^I compare the rules in the Student Eligibility & Prerequisite section$/ do
  #@activityOR = make AORequisitesData, :section => "Student Eligibility & Prerequisite"
  @prereq = make AOPreparationPrerequisiteRule, :term => "201208", :course => "ENGL304"
  @prereq.sepr_compare_catalog_co_ao_rule
end

Then /^I should be able to compare the CO Rule to the AO in the Student Eligibility & Prerequisite section$/ do
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.eligibility_prereq_edit_links.a(:text => /Compare/).exists?.should be_true
  end
end

Then /^there should be no rule in the Student Eligibility & Prerequisite section$/ do
  @prereq.open_agenda_section
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.agenda_management_section.text.should match /.*Student Eligibility & Prerequisite.*Rule.*\.\nCorequisite.*/m
  end
end

Then /^the created rule should exist in the Student Eligibility & Prerequisite section$/ do
  @prereq.open_agenda_section
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.agenda_management_section.text.should match @prereq.test_text("agenda", "ENGL101,ENGL478,HIST416,BSCI202,BSCI361,HIST110,Text to copy,free form text input value")
  end
end

Then /^the created rule should not exist in the Student Eligibility & Prerequisite section$/ do
  @prereq.open_agenda_section
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.agenda_management_section.text.should_not match @prereq.test_text("agenda", "ENGL101,ENGL478,HIST416,BSCI202,BSCI361,HIST110,Text to copy,free form text input value")
  end
end

Then /^the edited rule should exist in the Student Eligibility & Prerequisite section$/ do
  @prereq.open_agenda_section
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.agenda_management_section.text.should match @prereq.test_text("agenda", "lower-level English,ENGL478,HIST416,Text to copy,ARHU-English,ENGL101,BSCI202,BSCI361,HIST110,free form text input value")
  end
end

Then /^the edited rule should not exist in the Student Eligibility & Prerequisite section$/ do
  @prereq.open_agenda_section
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.agenda_management_section.text.should_not match @prereq.test_text("agenda", "lower-level English,ENGL478,HIST416,Text to copy,ARHU-English,ENGL101,BSCI202,BSCI361,HIST110,free form text input value")
  end
end

Then /^both rules for the Student Eligibility & Prerequisite section should be the same$/ do
  @prereq.open_agenda_section
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.view_tree.text.should match @prereq.test_compare_text("at least one in literature,ARHU-English required")
  end
end

Then /^both rules for the Student Eligibility & Prerequisite section should not be the same$/ do
  @prereq.open_agenda_section
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.view_tree.text.should_not match @prereq.test_compare_text("at least one in literature,ARHU-English required")
  end
end

Then /^all three rules for the Student Eligibility & Prerequisite section should be the same$/ do
  @prereq.open_agenda_section
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.compare_tree.text.should match @prereq.test_ao_compare_text("at least one in literature,ARHU-English required")
  end
end

Then /^all three rules for the Student Eligibility & Prerequisite section should not be the same$/ do
  @prereq.open_agenda_section
  on ActivityOfferingRequisites do |page|
    page.loading.wait_while_present
    page.compare_tree.text.should_not match @prereq.test_ao_compare_text("at least one in literature,ARHU-English required")
  end
end



When /^I update the manage activity offering agendas page$/ do
  on ManageAORequisites do |page|
    page.update_rule_btn
  end
end

Then /^there should be a new single course statement on the edit tab$/ do
  on ManageAORequisites do |page|
    page.edit_tree_section.text.should match /ENGL101/
  end
end