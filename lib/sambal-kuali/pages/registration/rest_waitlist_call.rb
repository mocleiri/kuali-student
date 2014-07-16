class WaitlistRestCallPage < PageFactory

  require 'json'
  page_url "#{$test_site}/services/CourseRegistrationClientService/getWaitlistRoster?termCode=201208&courseCode=HIST266&regGroupCode=1001"

  expected_element :service_data

  element(:service_data) { |b| b.pre }
  value(:service_text) { |b| b.service_data.text }

  def get_waitlist_roster
    waitlist_roster = JSON.parse(self.service_text)
    sleep 5
    waitlist_roster
  end

end
