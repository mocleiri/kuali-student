class CourseTermListObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable

  attr_accessor :courseterm_level,
                :formatlist_list




  def initialize(browser, opts={})
    @browser = browser
    defaults = {
        ao_courseterm_level:0,
        formatlist_list:[
        (make FormatListObject, :formatlist_level => 0),
        (make FormatListObject, :formatlist_level => 1)
    ]


    }


    set_options(defaults.merge(opts))

  end
end

