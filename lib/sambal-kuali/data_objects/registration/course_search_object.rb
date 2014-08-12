class CourseSearch < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Comparable

  attr_reader :search_string,
              :course_code,
              :course_prefix,
              :course_level,
              :selected_section

  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :search_string => "",
        :course_code => "",
        :course_prefix => "",
        :course_level => "",
        :selected_section => ""
    }
    options = defaults.merge(opts)
    set_options(options)
  end

  def inspect
    "Search string: #{@search_string}, course_code: #{@course_code}, course_level: #{@course_level}, course_prefix: #{@course_prefix}, selected_section: #{@selected_section}"
  end
  
  def search opts={}
    defaults = {
        :search_string => @search_string,
        :navigate=>false
    }
    options = defaults.merge(opts)

    return nil if options[:search_string].nil?

    visit CourseSearchPage if options[:navigate]
    on CourseSearchPage do |page|
      sleep 2
      page.go_to_results_page options[:search_string]
      # page.search_for_a_course options
    end
    set_options(options)
  end

  def select_facet(facet_type)
    on CourseSearchPage do |page|
      page.seats_avail_facet_div.wait_until_present
      case facet_type
        when "avail_seats" then
          page.toggle_seats_avail
          page.clear_seats_avail_facet.wait_until_present
        when "credit" then
          page.toggle_credits(@credit)
          page.clear_credit_facet.wait_until_present
        when "course_level"
          page.toggle_course_level(@course_level)
          page.clear_level_facet.wait_until_present
        when "course_prefix"
          page.toggle_course_prefix(@course_prefix)
          page.clear_prefix_facet.wait_until_present
      end
    end
    sleep 1
  end

  def clear_facet(facet_type)
    on CourseSearchPage do |page|
      page.seats_avail_facet_div.wait_until_present
      case facet_type
        when "avail_seats" then
          page.toggle_seats_avail if page.seats_avail_toggle.attribute_value("class") =~ /kscr-SearchFacet-option--Selected/i
          page.clear_seats_avail_facet.wait_while_present
        when "credit" then
          page.toggle_credits(@credit) if page.credits_toggle(@credit).attribute_value("class") =~ /kscr-SearchFacet-option--Selected/i
          page.clear_credit_facet.wait_while_present
        when "course_level" then
          page.toggle_course_level(@course_level) if page.course_level_toggle(@course_level).attribute_value("class") =~ /kscr-SearchFacet-option--Selected/i
          page.clear_level_facet.wait_while_present
        when "course_prefix" then
          page.toggle_course_prefix(@course_prefix) if page.course_prefix_toggle(@course_prefix).attribute_value("class") =~ /kscr-SearchFacet-option--Selected/i
          page.clear_prefix_facet.wait_while_present
      end
    end
  end

  def sort_results opts={}
    return nil if opts[:sort_key].nil?
    column = case opts[:sort_key]
               when "course code" then
                 "Code"
               when "title" then
                 "Title"
               when "credits" then
                 "Credits"
             end
    on CourseSearchPage do |page|
      wait_until { page.sort_selector(column).visible? }
      page.sort_results_by(column)
    end
  end
  
  def navigate_course_detail_page opts={}
    defaults = {
        :course_code => @course_code
    }
    options = defaults.merge(opts)

    return nil if options[:course_code].nil?
    # course_code = options[:course_code]
    on CourseSearchPage  do |page|
      page.course_desc_link_by_course(options[:course_code]).click
    end
  end

  def select_ao opts={}
    return nil if opts[:ao_type].nil? || opts[:ao_code].nil?

    on CourseDetailsPage do |page|
      page.toggle_ao_select(opts[:ao_code])
      wait_until { page.details_heading(opts[:ao_type]).text =~ /Selected/i }
    end
  end

  def edit opts={}
    edit_course_level opts
    edit_course_code opts
    edit_course_prefix opts
  end

  def edit_course_level opts={}
     return nil if opts[:course_level].nil?
     set_options(opts)
  end
  private :edit_course_level

  def edit_course_code opts={}
     return nil if opts[:course_code].nil?
     set_options(opts)
  end
  private :edit_course_code

  def edit_course_prefix opts={}
     return nil if opts[:course_prefix].nil?
     set_options(opts)
  end
  private :edit_course_prefix

  def set_section opts={}
    ###
    ### PAGE NO LONGER DISPLAYING SECTION NUMBER
    ### IS THIS A PERMANENT CHANGE??
    ###
    if opts[:section].nil?
      on CourseDetailsPage do |page|
        page.selected_section_span.wait_until_present
        @selected_section = page.selected_section.match(/\d+/,7)[0]
      end
    else
      @selected_section = opts[:section]
    end
  end

  def check_sort_order_in_all_pages opts={}
    defaults = {
        :sort_key=> "course_code",
        :sort_order=> "A"
    }
    options = defaults.merge(opts)
    sort_key = options[:sort_key]
    sort_column = case sort_key
                    when "course_code" then CourseSearchPage::COURSE_CODE
                    when "course_title" then CourseSearchPage::COURSE_DESC
                    when "credits" then CourseSearchPage::COURSE_CRED
                  end
    complete_list = []
    on CourseSearchPage do |page|
      complete_list = page.results_all_pages sort_column
    end
    complete_list_sorted_copy = complete_list.sort
    complete_list_sorted_copy.reverse! if options[:sort_order]=="D"
    return (complete_list == complete_list_sorted_copy)
  end
end