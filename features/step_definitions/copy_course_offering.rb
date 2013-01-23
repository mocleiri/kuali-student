When /^the registration groups are not copied$/ do
  @course_offering_copy.activity_offering_cluster_list.each do |cluster|
       on ManageRegistrationGroups do |page|
         page.get_cluster_status_msg(cluster.private_name).strip.should  match /.*No Registration Groups Generated.*/
      end
   end
end

Then /^the activity offering clusters? and assigned AOs are copied over with the course offering$/ do
  @course_offering_copy.manage
  @course_offering_copy.manage_registration_groups(false)

  on ManageRegistrationGroups do |page|
    clusters = page.cluster_div_list
    clusters.length.should == @course_offering_copy.activity_offering_cluster_list.length
  end

  @course_offering_copy.activity_offering_cluster_list.each do |cluster|
    cluster.assigned_ao_list.each do |ao_code|
      on ManageRegistrationGroups do |page|
        actual_aos = page.get_cluster_assigned_ao_list(cluster.private_name)
        actual_aos.sort.should == cluster.assigned_ao_list.sort
      end
    end
  end

end


Then /^I copy the course offering$/ do
  #TODO: once CO refactoring is done, merge this into 'deep_copy' method
  new_course_name = @course_offering.create_co_copy
  @course_offering_copy = make CourseOffering, :course=>new_course_name
  @course_offering_copy.activity_offering_cluster_list = @course_offering.activity_offering_cluster_list.sort
end