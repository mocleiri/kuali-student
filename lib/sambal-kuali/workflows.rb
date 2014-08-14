# Helper methods that don't properly belong elsewhere. This is
# a sort of "catch all" Module.
module Workflows

  # Site Navigation helpers...

  # def go_to_manage_soc
  #   visit Enrollment do |page|
  #     page.manage_soc
  #   end
  # end

  def log_in(user, pwd)
    current_user = ""

    visit KSFunctionalHome do |page|
      Watir::Wait.until { page.enrollment_link.present? || page.username_field.present? }
      current_user = page.current_logged_in_user_id
      if current_user == :no_user
        page.login_with user, pwd
      elsif current_user.downcase != user.downcase
        page.logout
        Watir::Wait.until { page.username_field.present? }
        page.login_with user, pwd
      end
    end
  end

  def log_out
    visit KSFunctionalHome do |page|
      Watir::Wait.until { page.enrollment_link.present? || page.username_field.present? }
      page.logout
    end
  end

end