class CmSubmitFieldsObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Utilities

  attr_accessor :subject_code,
                :course_number,
                :description_rationale,
                :proposal_rationale,
                :curriculum_oversight,
                :assessment_scale,
                :assessment_a_f,
                :assessment_notation,
                :assessment_letter,
                :assessment_pass_fail,
                :assessment_percentage,
                :assessment_satisfactory,
                :final_exam_type,
                :final_exam_rationale,
                :exam_standard,
                :exam_alternate,
                :exam_none,
                :outcome_list,
                :start_term



  def initialize(browser, opts={})
    @browser = browser
    defaults = {
        #COURSE INFORMATION
        subject_code:           "MATH",
        description_rationale:  random_alphanums(20, 'test description rationale '),
        proposal_rationale:     random_alphanums(20, 'test proposal rationale '),
        course_number:          "123",
        #GOVERNANCE
        curriculum_oversight:   '::random::',
        #COURSE LOGISTICS
        assessment_scale:           [:assessment_a_f, :assessment_notation, :assessment_letter, :assessment_pass_fail, :assessment_percentage, :assessment_satisfactory],
        final_exam_type:            [:exam_standard, :exam_alternate, :exam_none],
        final_exam_rationale:       random_alphanums(10,'test final exam rationale '),
        outcome_list: [
            (make CmOutcomeObject, :outcome_type => "Fixed", :outcome_level => 0, :credit_value=>(1..5).to_a.sample),
            (make CmOutcomeObject, :outcome_type => "Multiple",:outcome_level => 1, :credit_value => "#{(1..4).to_a.sample},#{(5..9).to_a.sample}"),
            (make CmOutcomeObject, :outcome_type => "Range", :outcome_level => 2, :credit_value => "#{(1..4).to_a.sample}-#{(5..9).to_a.sample}"),
            ],
        start_term: '::random::'
    }
    set_options(defaults.merge(opts))
  # random_checkbox and random_radio is used to select a random checkbox/radio on a page.
  # That will then be set the instance variable to :set, so that it can be used in the fill_out method for later tests
    random_assessment(@assessment_scale)
    random_radio(@final_exam_type)

  end

  def create

    on CmCourseInformation do |page|
      page.course_information unless page.current_page('Course Information').exists?
      page.subject_code.fit @subject_code
      page.auto_lookup @subject_code unless @subject_code.nil?
      fill_out page, :description_rationale, :proposal_rationale
    end
    determine_save_action

    on CmGovernance do |page|
      page.governance unless page.current_page('Governance').exists?
      page.curriculum_oversight.pick! @curriculum_oversight
    end
    determine_save_action

    on CmCourseLogistics do |page|
      page.course_logistics unless page.current_page('Course Logistics').exists?

      page.loading_wait
      #page.add_outcome unless @outcome_type.nil?
      # outcome_type needs to be done first because of how page loading is working
      #      page.outcome_type.pick! '::random::'
      #      fill_out page, :outcome_type
      fill_out page,
               :assessment_a_f, :assessment_notation, :assessment_letter, :assessment_pass_fail,
               :assessment_percentage, :assessment_satisfactory

      sleep 1
      #set_outcome_type
      if @outcome_list != nil
        @outcome_list.each do |outcome|
          outcome.create
        end
      end

      if @final_exam_type != nil
        fill_out page, :exam_standard, :exam_alternate, :exam_none
        #This 'UNLESS' is required for 'Standard Exam' which, does not have rationale and should skip filling in final_exam_rationale
        #if that radio is selected
        page.final_exam_rationale.wait_until_present unless page.exam_standard.set?
        page.final_exam_rationale.fit @final_exam_rationale unless page.exam_standard.set?
      end

    end
    determine_save_action

    on CmActiveDates do |page|
      page.active_dates unless page.current_page('Active Dates').exists?
      page.start_term.pick! @start_term unless start_term.nil?
    end
  end






  def edit (opts={})
    determine_edit_action

    on CmCourseInformation do |page|
      page.course_information unless page.current_page('Course Information').exists?
      page.subject_code.fit opts[:subject_code]
      page.auto_lookup opts[:subject_code] unless opts[:subject_code].nil?
      page.description_rationale.fit opts[:description_rationale]
      page.proposal_rationale.fit opts[:proposal_rationale]
      determine_save_action unless opts[:defer_save]
    end


    on CmGovernance do |page|
      page.governance unless page.current_page('Governance').exists?
      page.curriculum_oversight.pick! opts[:curriculum_oversight] unless opts[:curriculum_oversight].nil?
      determine_save_action unless opts[:defer_save]
    end


    on CmCourseLogistics do |page|
      page.course_logistics unless page.current_page('Course Logistics').exists?
      #Edit Assessment Scale\
      if opts[:assessment_scale] != nil
        reset_assessment(@assessment_scale)
        fill_out page,  :assessment_a_f, :assessment_notation, :assessment_letter, :assessment_pass_fail, :assessment_percentage, :assessment_satisfactory
      end
      #Edit Final Exam Status
      if opts[:final_exam_type] != nil
        random_radio(opts[@final_exam_type])
        fill_out page, :exam_standard, :exam_alternate, :exam_none
        #This 'UNLESS' is required for 'Standard Exam' which, does not have rationale and should skip filling in final_exam_rationale
        #if that radio is selected
        page.final_exam_rationale.wait_until_present unless page.exam_standard.set?
        page.final_exam_rationale.fit opts[:final_exam_rationale] unless page.exam_standard.set?
      end
      determine_save_action unless opts[:defer_save]
    end


    on CmActiveDates do |page|
      #Active Dates
      page.active_dates unless page.current_page('Active Dates').exists?
      page.start_term.pick! opts[:start_term] unless opts[:start_term].nil?
      determine_save_action unless opts[:defer_save]
    end
    set_options(opts)
  end

  def add_outcome (opts)
    defaults = {
        :outcome_type => "Fixed"
    }
    options = defaults.merge(opts)
    options[:outcome].create
    @outcome_list << options[:outcome]
    determine_save_action
  end


  def random_assessment(pass_in_an_array)
     @sample_assessment = pass_in_an_array.sample unless pass_in_an_array.nil?
     set(@sample_assessment, :set)  unless pass_in_an_array.nil?
  end



  def reset_assessment(pass_in_an_array)
     set(@sample_assessment, :clear) unless pass_in_an_array.nil?
     pass_in_an_array.delete(@sample_assessment)
     @new_sample_assessment = pass_in_an_array.sample
     set(@new_sample_assessment, :set) unless pass_in_an_array.nil?
  end

  def random_radio(pass_in_an_array)
    @sample_final_exam = pass_in_an_array.sample unless pass_in_an_array.nil?
    set(@sample_final_exam, :set)  unless pass_in_an_array.nil?
  end


end
