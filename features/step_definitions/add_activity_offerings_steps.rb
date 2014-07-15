When /^I add an Activity Offering to an existing Course Offering$/ do
  course_offering = make CourseOffering, :course=>"CHEM276"
  @new_ao = course_offering.create_ao :ao_obj => (make ActivityOfferingObject, :format => "Lecture/Discussion")
end


Then /^the new Activity Offering is shown in the list of AOs$/ do
  on(ManageCourseOfferings).codes_list.should include(@new_ao.code)
end


When /^I copy an AO with Actual Scheduling Information$/ do

  # in ref-data this CO has ASIs but no RSIs (copy an ASI first if you need an RSI)
  @course_offering_source = make CourseOffering, :course => "CHEM276"
  @course_offering_source.initialize_with_actual_values

  @course_offering = create CourseOffering, :create_by_copy => @course_offering_source
  @ao_copy = make ActivityOfferingObject, :code => 'A',
                    :parent_cluster => @course_offering.default_cluster
end

When /^I copy an AO with Requested Scheduling Information$/ do
  # use the AO we copied in the previous step as the source for a new copy
  # this AO should have RSIs but no ASIs
  @course_offering_source = @course_offering
  @course_offering_source.initialize_with_actual_values
  @ao_copy = @course_offering_source.copy_ao :ao_code => 'A'
end

Then /^the "(ASI|RSI)s" are successfully copied as RSIs in the new AO$/ do |source_scheduling_information_type|

  source_scheduling_information = nil
  if source_scheduling_information_type == "ASI"
    source_scheduling_information = @course_offering_source.get_ao_obj_by_code('A').actual_scheduling_information_list[0]
  else
    source_scheduling_information = @course_offering_source.get_ao_obj_by_code('A').requested_scheduling_information_list[0]
  end
  source_scheduling_information.nil?.should be_false

  #@ao_copy.manage_parent_co
  @ao_copy.edit :defer_save => true

  on ActivityOfferingMaintenance do |page|
    page.view_requested_scheduling_information
    page.requested_sched_info_table.rows.size.should be > 1 # should be more than just header row
    row = page.requested_sched_info_table.rows[1]
    page.get_requested_sched_info_days(row).delete(' ').should == source_scheduling_information.days
    page.get_requested_sched_info_start_time(row).delete(' ').should == "#{source_scheduling_information.start_time}#{source_scheduling_information.start_time_ampm}"
    page.get_requested_sched_info_end_time(row).delete(' ').should == "#{source_scheduling_information.end_time}#{source_scheduling_information.end_time_ampm}"
    page.get_requested_sched_info_facility(row).should == source_scheduling_information.facility_long_name
    page.get_requested_sched_info_room(row).should == source_scheduling_information.room
    page.cancel
  end
end






