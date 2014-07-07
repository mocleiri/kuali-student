class CourseActivityOfferingObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable

  attr_accessor :activity_offering_code,
                :activity_offering_instructor,
                :activity_offering_days,
                :activity_offering_time,
                :activity_offering_location,
                :activity_offering_seats,
                :activity_offering_additional_details,
                :activity_offering_select







  def initialize(browser, opts={})
    @browser = browser
    defaults = {
        :activity_offering_code=>"A",
        :activity_offering_instructor=>"ROBINSON, CHARLES",
        :activity_offering_days=>"MWF",
        :activity_offering_time=>"11:00 AM - 11:50 AM",
        :activity_offering_location=>"TWS 0221",
        :activity_offering_seats=>"0/35",
        :activity_offering_additional_details=>" "




    }
    set_options(defaults.merge(opts))

  end







end