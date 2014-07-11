class FormatOfferingObject< DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable

  attr_accessor :fo_format_level,
                :ao_list




  def initialize(browser, opts={})
    @browser = browser
    defaults = {
        formatlist_level:0,
        ao_list:[
            (make ActivityOfferingObject , :ao_activityoffering_level => 0),
            (make ActivityOfferingObject , :ao_activityoffering_level => 1),
            (make ActivityOfferingObject , :ao_activityoffering_level => 2)
        ]



    }

    set_options(defaults.merge(opts))

  end
end

