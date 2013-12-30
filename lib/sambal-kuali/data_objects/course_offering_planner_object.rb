class CoursePlanner

  include Foundry
  include DataFactory
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable

  attr_accessor :course_code,:credit,:notes

  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :course_code=>"BSCI430",
        :credit=>5,
        :notes=>"#{random_alphanums(20).strip}_pub"
    }
    options = defaults.merge(opts)
    set_options(options)
  end

  def add_course_to_current_term
    navigate_to_course_planner_home
    sleep 5
    on CoursePlannerPage do |page|
      page.current_term_add
      sleep 2
      page.course_code_text.set @course_code
      page.notes.set @notes
      page.add_to_plan
      puts page.growl_text

    end
  end


  def add_course_to_future_term
    navigate_to_course_planner_home
    sleep 5
      on CoursePlannerPage do |page|
          page.future_term_add
          page.course_code_text.set @course_code
          page.credit.set @credit
          page.notes.set @notes
          page.add_to_plan
      end
  end

  def edit_plan_item
    on CoursePlannerPage do |page|
      sleep 2
      page.course_code_current_term_click
      page.course_code_current_term_edit_plan_item_click
      #page.click_on_ccode_current_term
      #sleep 2
      #page.edit_plan_item_action
      sleep 2
      puts page.view_cterm_course_planner_notes


    end

  end

end


