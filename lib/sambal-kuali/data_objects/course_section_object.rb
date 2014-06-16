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
        :course_detail_header=>"ENGL201 Course Sections",
        :term_and_course_offering=>"Spring 2014 (1 course offerings)",
        :course_offering=>"ENGL206 - Shakespeare",
        :course_description=>"Course description: Wide range of texts, genres, and themes from ancient and medieval Western traditions. Study of cultural, historical, and artistic forces shaping traditions, and the influence and relevance of those traditions to life in twenty-first century.",
        :activity_offering_code=>"A",
        :activity_offering_instructor=>"TATE, THOMAS",
        :activity_offering_days=>"MF",
        :activity_offering_time=>"12:00 PM - 12:50 PM",
        :activity_offering_location=>"Undergraduate Library 0109",
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