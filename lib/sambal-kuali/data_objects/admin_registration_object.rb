class AdminRegistrationData < DataFactory
  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable

  attr_accessor :student_id

  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :student_id => "ks-2094"
    }

    options = defaults.merge(opts)

    set_options(options)
  end

  def create
    go_to_admin_registration
     on AdminRegistration do |page|
       page.student_info_input.set @student_id
       page.student_info_go
     end
  end
end