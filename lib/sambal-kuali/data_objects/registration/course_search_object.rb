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