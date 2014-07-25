class WaitlistRestCallPage < PageFactory

  require 'json'

  # expected_element :service_data

  element(:service_data) { |b| b.pre }
  value(:service_text) { |b| b.service_data.text }

  def get_waitlist_roster(term_code, course_code, reg_group_code)
    page_url = "#{$test_site}/services/CourseRegistrationAdminClientService/waitlistRoster?termCode=#{term_code}&courseCode=#{course_code}&regGroupCode=#{reg_group_code}"
    @browser.goto page_url
    waitlist_roster = JSON.parse(self.service_text)
    sleep 5
    waitlist_roster
  end

end
