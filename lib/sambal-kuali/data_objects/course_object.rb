class CmCourseObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Utilities

  attr_accessor :course_code,
                :search_term,
                # COURSE INFORMATION
                :course_title,
                :transcript_course_title,
                :description,
                # GOVERNANCE
                :campus_location,
                :curriculum_oversight,
                # COURSE LOGISTICS
                :assessment_scale,
                :audit,
                :pass_fail_transcript_grade,
                :final_exam_status,
                :outcome_list,
                :format_list,
                # ACTIVE DATES
                :start_term,
                :pilot_course,
                :course_state






  def initialize(browser, opts={})
    @browser = browser
    defaults = {

    }
    set_options(defaults.merge(opts))

  end


  def search_for_course
    navigate_rice_to_cm_home
    navigate_to_find_course
    on CmFindACoursePage do |search|
      search.course_code.set search_term
      search.find_courses
    end
  end


  def view_course
    search_for_course
    on CmFindACoursePage do |view|
      view.view_course(@course_code)
    end
  end

end


