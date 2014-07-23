# Helper methods that don't properly belong elsewhere. This is
# a sort of "catch all" Module.
module Workflows

  # Site Navigation helpers...
#  def navigate_to_course_search_home
 #   on KSRicePortal do |page|
     # page.course_search_home
   # end
  #end

  def navigate_to_course_search_home
    visit CourseSearch do |page|
      #page.course_search_home
    end
  end


 def  navigate_to_course_planner_home
   visit CoursePlannerPage do |page|
      puts page
     end
   end


  def navigate_to_i18n_poc_home
    on KSRicePortal do |page|
      page.i18n_poc_home
    end
  end




  def navigate_to_maintenance_portal
    on KSFunctionalHome do |page|
      page.ks_maintenance_link.wait_until_present
      page.ks_maintenance
#      page.login_with "student",""
      sleep 2
    end
  end

  def log_in(user, pwd)
    current_user = ""

    visit KSFunctionalHome do |page|
      Watir::Wait.until { page.enrollment_link.present? || page.username_field.present? }
      current_user = page.current_logged_in_user_id
      if current_user == :no_user
        page.login_with user, pwd
      elsif current_user.downcase != user.downcase
        page.logout
        visit Login do |page|
          page.login_with user, pwd
        end
      end
    end
  end

end