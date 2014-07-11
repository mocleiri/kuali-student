class FormatListObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable

  attr_accessor :formatlist_level,
                :fo_list




  def initialize(browser, opts={})
    @browser = browser
    defaults = {
       formatlist_level:0,
       fo_list:[
        (make FormatOfferingObject , :fo_format_level => 0),
        (make FormatOfferingObject , :fo_format_level => 1),
        (make FormatOfferingObject , :fo_format_level => 2)


    ]



    }

    set_options(defaults.merge(opts))

  end
end

