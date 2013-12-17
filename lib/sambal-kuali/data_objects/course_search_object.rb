class CourseOffering

  include Foundry
  include DataFactory
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable


  COURSE_ARRAY = 0
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



  def set_search_entry
    navigate_to_course_search_home
    on CourseSearch do |page|
     page.search_for_course.set @course_code
    end
  end


  def course_search (text=@course_code)
    navigate_to_course_search_home
    on CourseSearch do |page|
    page.search_for_course.set text
    page.search
    end
  end

  def clear_search
    on CourseSearch do |page|
      page.clear
    end
  end

  end