class Login < PageFactory

  page_url "#{$test_site}/login.jsp"

  element(:username_field) { |b| b.textarea(:name=>"j_username") }
  element(:password_field) { |b| b.textarea(:name=>"j_password") }
  element(:login_button) { |b| b.button(:value=>"Login") }

  def login_with username, password
    username_field.set username
    password_field.set password
    login_button.click
  end

end