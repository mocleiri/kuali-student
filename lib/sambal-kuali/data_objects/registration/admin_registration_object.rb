class AdminRegistrationData < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows

  attr_accessor :student_id, :term_code, :course_section_codes,
                :term_description

  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :student_id => "ks-2094",
        :term_code => nil,
        :term_description => nil,
        :course_section_codes => collection('ARCourseSection')
    }

    options = defaults.merge(opts)

    set_options(options)
  end

  def search
    go_to_admin_registration
  end

  def create
    search

    on AdminRegistration do |page|
      page.student_info_input.set @student_id
      page.student_info_go

      if @term_code != nil
        page.change_term_input.set @term_code
        page.change_term_go
        
        page.loading.wait_while_present
        if page.confirm_term_popup_section.visible?
          page.confirm_term_continue
        end
      end
    end
  end

  def add_course_section opts
    opts[:course_section_obj].parent = self
    opts[:course_section_obj].create
    @course_section_codes << opts[:course_section_obj]
  end

end

class ARCourseSectionObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows

  attr_accessor :parent, :course_code, :section,
                :course_description

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
        :confirm_registration => false
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