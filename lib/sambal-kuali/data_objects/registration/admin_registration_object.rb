class AdminRegistrationData < DataFactory
  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable

  attr_accessor :student_id,
                :term_code,
                :course_code,
                :section,
                :term_description,
                :course_description

  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :student_id => "ks-2094",
        :term_code => nil,
        :course_code => nil,
        :section => nil,
        :term_description => nil,
        :course_description => nil
    }

    options = defaults.merge(opts)

    set_options(options)
  end

  def create
    go_to_admin_registration
    on AdminRegistration do |page|
      page.student_info_input.set @student_id
      page.student_info_go

      if @term_code != nil
        page.change_term_input.set @term_code
        page.change_term_go

        page.course_code_input.when_present.set @course_code if @course_code != nil
        page.section_code_input.when_present.set @section if @section != nil
      end
    end
  end

end