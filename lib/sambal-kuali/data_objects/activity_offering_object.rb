class ActivityOfferingObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable
  attr_accessor :ao_code,
                :ao_instructor,
                :ao_days,
                :ao_location,
                :ao_seatsopen,
                :ao_additional,
                :ao_select,
                :ao_courseoffering_level,
                :ao_coursedescription_level,
                :ao_formatlist_level,
                :ao_formatoffering_level,
                :ao_activityoffering_level




  def initialize(browser, opts={})
    @browser = browser
    defaults = {
        ao_code: "A",
        ao_instructor: "SUMMERS, RICHARD",
        ao_days:"T",
        ao_location:"CHM 1407",
        ao_seatsopen:"0/156",
        ao_additional:"",
        ao_select:"on",
        ao_activityoffering_level:0

    }

    set_options(defaults.merge(opts))

  end
  end

