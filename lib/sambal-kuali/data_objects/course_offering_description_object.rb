class CourseOfferingDescriptionObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable

  attr_accessor :courseofferingdescription_level,
                :course_term_list





  def initialize(browser, opts={})
    @browser = browser
    defaults = {
           courseofferingdescription_level:0,
    course_term_list:[
        (make CourseTermListObject, :courseterm_level => 0),
        (make CourseTermListObject, :courseterm_level => 1)

    ]


    }

    set_options(defaults.merge(opts))

  end
end

