class RestLoginPage < PageFactory

  def login_as (userid)
    url = "#{$test_site}/services/DevelopmentLoginClientService/login?userId=#{userid}&password=#{userid}"
    @browser.goto url
  end

end
