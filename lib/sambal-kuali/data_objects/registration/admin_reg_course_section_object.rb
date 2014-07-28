class ARCourseSectionObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows

  attr_accessor :parent, :course_code, :section,
                :course_description,:course_default_credits,
                :course_default_reg_options,
                :course_default_effective_date

  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :parent => nil,
        :navigate_to_page => false,
        :course_code => "ENGL304",
        :section => "1001",
        :course_description => nil,
        :add_new_line => false,
        :register => false,
        :confirm_registration => false,
        :course_default_credits => nil,
        :course_default_reg_options => nil,
        :course_default_effective_date => right_now[:date_w_slashes]
    }

    options = defaults.merge(opts)

    set_options(options)
  end

  def create
    @parent.search if @navigate_to_page

    on AdminRegistration do |page|
      if @parent.term_code != nil
        page.course_addline if @add_new_line

        page.course_code_input.when_present.set @course_code
        page.section_code_input.when_present.set @section

        if @register
          page.course_register
          page.confirm_registration if @confirm_registration
        end
      end
    end
  end

  def delete opts
    @parent.search if opts[:navigate_to_page]

    on AdminRegistration do |page|
      page.course_delete opts[:index]
      page.deleting.wait_while_present
    end

    @parent.course_section_codes.delete self
  end

end

class ARCourseSectionCollection < CollectionsFactory

  contains ARCourseSectionObject

end