class CmCourseProposalObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Utilities

  # Course Information, Governance, Course Logistics, Active Dates completed
  attr_accessor :proposal_title, :course_title,  :subject_code,
                :optional_fields, :approve_fields, :submit_fields,
        :cross_listed_course_list,
        :jointly_offered_course_list,
        :version_code_list,
        :description_rationale, :proposal_rationale,
        #
        :outcome_list,
        :format_list,

        #Learning Objectives
        :learning_objective_list,
        #Course Requisites
        :course_requisite_list,

        :author_list,
        :supporting_doc_list,

        #Save
        :create_new_proposal,
        :save_proposal,
        :defer_save,
        :blank_proposal,
        :create_basic_proposal,
        :create_optional_fields,
        :curriculum_review_process



  def initialize(browser, opts={})
    @browser = browser
    defaults = {
        #COURSE INFORMATION
        proposal_title:             random_alphanums(10,'test proposal title '),
        course_title:               random_alphanums(10, 'test course title '),
        curriculum_review_process:  nil,
        create_new_proposal:        true,
        blank_proposal:             false,
        create_basic_proposal:      false,
        create_optional_fields:     false,
        save_proposal:              true,
        defer_save:                 false
    }
    set_options(defaults.merge(opts))

    # random_checkbox and random_radio is used to select a random checkbox/radio on a page.
    # That will then be set the instance variable to :set, so that it can be used in the fill_out method for later tests

  end


  def create
    navigate_rice_to_cm_home
    navigate_to_create_course_proposal
    set_curriculum_review

    if @create_optional_fields
      create_course_continue
      create_proposal_optional
    elsif @create_new_proposal
      create_course_continue
      create_course_proposal_required unless @blank_proposal
      determine_save_action unless @defer_save
    end

  end


  def edit (opts={})
    determine_edit_action

    on CmCourseInformation do |page|
      page.course_information unless page.current_page('Course Information').exists?
      page.proposal_title.fit opts[:proposal_title]
      page.course_title.fit opts[:course_title]
    end

    determine_save_action unless opts[:defer_save]

    set_options(opts)
  end

  def create_basic_proposal
    on CmCourseInformation do |page|
      page.course_information unless page.current_page('Course Information').exists?
      fill_out page, :proposal_title, :course_title
    end

    determine_save_action unless @defer_save

  end

  def create_proposal_optional
    create_basic_proposal

    @optional_fields.each do |optional|
     optional.create
    end

  end

  def delete_requisite (opts={})
    on CmCourseRequisitesPage do |page|
      page.course_requisites unless page.current_page('Course Requisites').exists?
      section = opts[:requisite_type]
      case section
        when "Student Eligibility & Prerequisite"
          #STUDENT ELIGIBILITY #A,G,M,S,Y
          begin
            page.delete_rule_student_eligibility
          rescue Exception => e
            page.delete_rule('AE')
          end
        when "Corequisite"
          page.delete_rule_corequisite
        when "Recommended Preparation"
          page.delete_rule_recommended_prep
        when "Antirequisite"
          page.delete_rule_antirequisite
        when "Repeatable for Credit"
          page.delete_rule_repeatable_for_credit
        when "Course that Restricts Credits"
          page.delete_rule_restricts_credits
        else
          raise "No requisite rule type defined!"
      end
    end
  end


  def create_course_proposal_required_for_save
    on CmCourseInformation do |page|
      page.course_information unless page.current_page('Course Information').exists?

      fill_out page, :proposal_title, :course_title
      determine_save_action
    end
  end


  def create_course_proposal_required
    on CmCourseInformation do |page|
      page.course_information unless page.current_page('Course Information').exists?
      fill_out page, :proposal_title, :course_title
    end
    determine_save_action


      unless @cross_listed_course_list.nil?
        @cross_listed_course_list.each do |cross_listed_course|
          cross_listed_course.create
        end
      end

      unless @jointly_offered_course_list.nil?
        @jointly_offered_course_list.each do |jointly_offered_course|
          jointly_offered_course.create
        end
      end

      unless @version_code_list.nil?
        @version_code_list.each do |version_code|
          version_code.create
        end
      end

      unless @submit_fields.nil?
        @submit_fields.each do |submit_fields|
          submit_fields.create
        end
      end

      unless @approve_fields.nil?
        @approve_fields.each do |approve_fields|
          approve_fields.create
        end
      end

      unless @learning_objective_list.nil?
          @learning_objective_list.each do |learning_objective|
          learning_objective.create
          end
      end

      unless @course_requisite_list.nil?
        @course_requisite_list.each do |course_requisite|
          course_requisite.create
        end
      end


      unless @author_list.nil?
        @author_list.each do |authors|
          authors.create
        end
      end

      unless @supporting_doc_list.nil?
        @supporting_doc_list.each do |supporting_doc|
          supporting_doc.create
        end
      end

  determine_save_action unless @defer_save

  end # required proposal

  def course_proposal_nonrequired
    on CmCourseInformation do |page|

      page.course_information unless page.current_page('Course Information').exists?
      page.loading_wait
      page.expand_course_listing_section unless page.collapse_course_listing_section.visible?


      page.add_another_course_listing unless @course_listing_subject.nil? and @course_listing_number.nil?
      fill_out page, :course_listing_number

      page.course_listing_subject.fit @course_listing_subject
      page.auto_lookup @course_listing_subject unless @course_listing_subject.nil?

      #Joint offering adding and instructor adding in private methods do to complexity of advanced search adding
      add_joint_offering
      add_instructor

      page.save_and_continue
    end

    on CmGovernance do |page|
      page.governance unless page.current_page('Governance').exists?

      # Admin organization in private method do to complexity of advanced search adding
      adding_admin_organization

      page.save_and_continue
    end

    on CmCourseLogistics do |page|
      page.course_logistics unless page.current_page('Course Logistics').exists?
      fill_out page, :term_any, :term_fall, :term_spring, :term_summer,
               :audit, :pass_fail_transcript_grade,
               :duration_type, :duration_count,
               :activity_contacted_hours, :activity_class_size,
               :activity_duration_count, :activity_frequency, :activity_duration_type
      page.save_and_continue
    end

    on CmLearningObjectives do |page|
      page.learning_objectives unless page.current_page('Learning Objectives').exists?
      # TODO:: NEED TO MAKE TESTS FOR THIS PAGE
      page.save_and_continue
    end

    unless @course_requisite_list.nil?
      @course_requisite_list.each do |requisite|
        requisite.create
      end
    end

    on CmAuthorsCollaborators do |page|
      page.authors_collaborators unless page.current_page('Authors Collaborators').exists?
      # Adding author name in private method do to complexity of advanced search adding
      adding_author_name

      fill_out page, :author_permission, :action_request, :author_notation
      page.add_author
      page.save_and_continue
     end

  end # non-required


  #Used for Advanced Search to "Return Value" of the result that matches
  #Defaults to 4th Column to match instructor display name but can be altered for different advacned search results by passing in a number of the column
  def return_search_result(search_result_value_to_match, row_number=3)
    on CmCourseRequistitesPage do |page|
    page.search_results_table.rows.each do |row|
        if row.cells[row_number].text == search_result_value_to_match
          row.cells[0].link(text: 'return value').click
          break
        end
      end
    end
  end

  def cancel_create_course
    on CmCreateCourseStart do |page|
      page.cancel
    end
  end

  def cancel_course_proposal
     on CmCourseInformation do |page|
       page.cancel
     end
  end

  def set_curriculum_review
  on CmCreateCourseStart do |create|
    create.curriculum_review_process.fit checkbox_trans[@curriculum_review_process] unless @curriculum_review_process.nil?
  end
  end

  def create_course_continue
    on CmCreateCourseStart do |create|
      create.continue
    end
  end

  def create_course_proposal_required_fields
  on CmCourseInformation do |create|
    fill_out create, :proposal_title, :course_title
  end
  end



  def search(search_text)
    navigate_to_find_course_proposal
       on FindProposalPage do |page|
          page.name.wait_until_present
          page.name.set search_text
          page.find_a_proposal
       end

  end

  def load_comments_action
    on CmCourseInformation do |page|
      page.load_comments
    end
  end


  def load_comments_on_review_page
    on CmReviewProposal do |page|
      page.load_comments
    end
  end

  def load_decisions_action
    on CmCourseInformation do |page|
      page.load_decisions
    end
  end


  def review_proposal_action
    on FindProposalPage do |page|
      page.review_proposal_action_link(@proposal_title)
      page.loading_wait
    end
  end

  def edit_proposal_action
    on FindProposalPage do |page|
      page.edit_proposal_action(@proposal_title)
      page.loading_wait
    end
  end

  def edit_course_information
    on CmReviewProposal do |page|
      page.edit_course_information
      page.loading_wait
    end
  end

  def determine_edit_action
    #if current page is find a proposal click the pencil icon


    on CmReviewProposal do |page|
      if page.proposal_title_element.exists?
        edit_course_information
      end
    end

    #if current page is review proposal click the course information edit icon

    on FindProposalPage do |page|
      if page.name.exist?
        edit_proposal_action
      end

    end

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

  def add_author (opts)
    defaults = {

    }
    options = defaults.merge(opts)
    options[:author].create
    @author_list << options[:author]
  end

  def add_learning_objective (opts)
    defaults = {

    }
    options = defaults.merge(opts)
    options[:learn_obj].create
    @learning_objective_list << options[:learn_obj]
    determine_save_action
  end

  def add_supporting_doc (opts)
    defaults = {

    }
    options = defaults.merge(opts)
    options[:supporting_doc].create
    @supporting_doc_list << options[:supporting_doc]
    determine_save_action unless opts[:defer_save]
  end

  def submit_proposal
    on(CmCourseInformation).review_proposal
    on(CmReviewProposal).submit_proposal
    on(CmReviewProposal).submit_confirmation
  end

  def submit_incomplete_proposal
    on(CmCourseInformation).review_proposal
    on(CmReviewProposal).submit_proposal
  end

  def submit_button_disabled
    on(CmCourseInformation).review_proposal
    begin
      on(CmReviewProposal).submit_proposal
    rescue Exception => e
      #element should be disabled
      (e.message.include? "object is disabled").should == true
    end
  end

  #-----
  private
  #-----

  def checkbox_trans
     { "Yes" => :set, "No" => :clear }
  end



  def random_radio(pass_in_an_array)
    @sample_radio = pass_in_an_array.sample unless pass_in_an_array.nil?
    set(@sample_radio, :set)  unless pass_in_an_array.nil?
  end


#COURSE INFORMATION
  def add_joint_offering
    on CmCourseInformation do |page|
      if @joint_offering_adding_data == 'auto_lookup'
        page.add_another_course
        page.joint_offering_number.fit @joint_offering_number
        page.auto_lookup @joint_offering_number unless @joint_offering_number.nil?
      end

      if @joint_offering_adding_data == 'adv_given_name' or @joint_offering_adding_data == 'adv_course_code' or @joint_offering_adding_data == 'adv_plain_text'
        page.add_another_course
        page.joint_offering_advanced_search
        page.adv_given_name.wait_until_present
        page.adv_course_code.fit @joint_offering_course_code if @joint_offering_adding_data == 'adv_course_code'
        page.adv_given_name.fit @joint_offering_name if @joint_offering_adding_data == 'adv_given_name'
        page.adv_plain_text_description.fit @joint_offering_description if @joint_offering_adding_data == 'adv_plain_text'
        page.adv_search
        page.adv_return_value @joint_offering_course_code
      end
    end
  end

  def add_instructor
    on CmCourseInformation do |page|
      if instructor_adding_method == 'advanced' or instructor_adding_method == 'adv_name' or instructor_adding_method == 'adv_username'
        page.instructor_advanced_search
        page.adv_name.fit @instructor_last_name if instructor_adding_method == 'adv_name' or instructor_adding_method == 'advanced'
        page.adv_username.fit @instructor_username if instructor_adding_method == 'adv_username' or instructor_adding_method == 'advanced'
        page.adv_search
        page.adv_return_value_instructor @instructor_display_name
      end

      if instructor_adding_method == 'auto_lookup'
        page.instructor_name.fit @instructor_last_name
        page.auto_lookup @instructor_display_name unless @instructor_display_name.nil?

      end

      # DUE TO RICE ISSUE CODE NEEDS TO WAIT FOR FIELD TO DISPLAY THE RETURN RESULTS FOR ADV SEARCH
      # So we wait until the name field = returned value
      page.instructor_name.text == @instructor_display_name

      page.instructor_add unless @instructor_last_name.nil?
    end
  end

#GOVERNANCE
  def adding_admin_organization
    on CmGovernance do |page|
      if admin_org_adding_method == 'auto_lookup'
        page.administering_organization.fit @administering_organization
        #TODO: uncomment this when bug KSCM-1204 is fixed for auto lookup on administering org text field
        #page.auto_lookup @administering_organization unless @administering_organization.nil?
        page.organization_add unless @administering_organization.nil?
      end

      if admin_org_adding_method == 'advanced'
        page.adv_search_admin_org
        page.adv_identifier.fit @adv_admin_org_identifier
        page.adv_org_name.fit @adv_admin_org_name
        page.adv_abbreviation.fit @adv_admin_org_abbreviation
        page.adv_search
        page.adv_admin_org_return_value(@adv_admin_org_name, @adv_admin_org_abbreviation)
        page.organization_add unless @administering_organization.nil?
      end
    end
  end

  #AUTHORS AND COLLABORATORS
    def adding_author_name
      on CmAuthorsCollaborators do |page|
        # Need to use auto lookup because when watir types in the parentheses are removed from text field
        if author_name_method == 'auto_lookup'
          page.author_name.fit @author_name_search
          page.auto_lookup @author_display_name
        end

        if author_name_method == 'advanced_name' or author_name_method == 'advanced_username'
          page.advanced_search
          page.adv_name.fit @author_name_search if author_name_method == 'advanced_name'
          page.adv_username_capD.fit @author_username_search if author_name_method == 'advanced_username'
          page.adv_search
          page.adv_return_value_name @author_display_name
        end
      end
    end
  
    def cs_course_proposal_required
      on CmCourseInformation do |page|
        page.course unless page.current_page('Course').exists?

        page.expand_course_listing_section unless page.collapse_course_listing_section.visible?

        fill_out page, :description_rationale, :proposal_rationale
        page.save_progress
      end

      on CmCourseLogistics do |page|
        page.logistics unless page.current_page('Logistics').exists?

        page.loading_wait

        fill_out page,
                 :assessment_a_f, :assessment_notation, :assessment_letter, :assessment_pass_fail,
                 :assessment_percentage, :assessment_satisfactory

        fill_out page, :exam_standard, :exam_alternate, :exam_none

        #This 'UNLESS' is required for 'Standard Exam' which, does not have rationale and should skip filling in final_exam_rationale
        #if that radio is selected
        page.final_exam_rationale.fit @final_exam_rationale unless page.exam_standard.set?
        page.save_progress
      end

      on CmCourseFinancials do |page|
        page.financials unless page.current_page('Financials').exists?
        page.loading_wait
        fill_out page, :course_fees

        page.save_progress

      end

    end
  
  

end #class




