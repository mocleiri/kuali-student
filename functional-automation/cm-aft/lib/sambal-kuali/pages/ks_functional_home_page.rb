class KSFunctionalHome < BasePage

  page_url "#{$test_site}/kr-krad/launch?methodToCall=start&viewId=ksFunctionalHomeView"
  wrapper_elements

  element(:enrollment_link) { |b| b.div(id: "KS-StudentHome-Enrollment") }
  action(:enrollment_home) { |b| b.enrollment_home_link.click }

  #action(:kuali_student_home) { |b| b.link(text: "Kuali Student Home").click }
  action(:cm_home) { |b| b.div(id: "KS-StudentHome-CurriculumManagement").click }
  action(:ks_maintenace) { |b| b.div(id: "KS-StudentHome-KSMaintenance").click }

  element(:username_field) { |b| b.text_field(:name=>"j_username") }
  element(:password_field) { |b| b.text_field(:name=>"j_password") }
  element(:login_button) { |b| b.button(:value=>"Login") }
  #action(:logout) { |b| b.button(value: "Logout").click }

  def login_with username, password
    username_field.set username
    password_field.set password
    login_button.click
    enrollment_link.wait_until_present
    sleep 5
  end


  def current_logged_in_user_id
    user = ""
    begin
      user = logged_in_user
    rescue Watir::Exception::UnknownObjectException
      user = :no_user
    end
    user
  end
end