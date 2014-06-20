class CourseSectionObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable

  attr_accessor :course_detail_header,
                :term_and_course_offering,
                :course_offering,
                :course_description,
                :activity_offering_code,
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
        :course_detail_header=>"ENGL202 Course Sections",
        :term_and_course_offering=>"Spring 2014 (1 course offering)",
        :course_offering=>"ENGL202 - Inventing Western Literature: Renaissance to Modern (3 cr)",
        :course_description=>"Course description: Wide range of texts",
        :activity_offering_code=>"A",
        :activity_offering_instructor=>"ROBINSON, CHARLES",
        :activity_offering_days=>"MWF",
        :activity_offering_time=>"11:00 AM - 11:50 AM",
        :activity_offering_location=>"TWS 0221",
        :activity_offering_seats=>"0/35",
        :activity_offering_additional_details=>" ",




    }
    set_options(defaults.merge(opts))

  end

  def create

  end





  def edit (opts={})

    set_options(opts)
  end



  def delete

  end


end