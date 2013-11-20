class CourseOffering

  include Foundry
  include DataFactory
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable



  attr_accessor :course_code

  # initialize is generally called using TestFactory Foundry .make or .create methods
  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :course_code=>"ENGL101"
    }
    options = defaults.merge(opts)
    set_options(options)
  end

  def navigate_to_course_search_home
    on KSFunctionalHome do |page|
      page.course_search_home
    end
  end

  def set_search_entry
   on CourseSearch do |page|
     page.search_for_course.set @course_code
    end
  end


  def course_search
    on CourseSearch do |page|
    page.search
    end
  end


    def clear_search_entry
    on CourseSearch do |page|
      page.clear
    end
  end

  def verify_search_entry_clear
    on CourseSearch do |page|
      page.search_for_course.text == ""
    end
  end

  def clear_search_result
    on CourseSearch do |page|
      page.clear
    end
  end

  def verify_search_result_clear
    on CourseSearch do |page|
      page.results_list ==nil
    end
  end


end