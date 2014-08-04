class CourseSearchPage < RegisterForCourseBase

  def go_to_results_page (search_string)
    page_url = "#{$test_site}/registration/index.jsp#/search/#{search_string}"
    @browser.goto page_url
    course_input_div.wait_until_present
  end

  # Search input
  element(:course_input_div){ |b| b.div(class: "kscr-Responsive-searchFormWrapper kscr-SearchForm") }
  element(:course_input){ |b| b.text_field(id: "courseSearchCriteria") }
  element(:course_input_button) { |b| b.button(id: "searchSubmit") }
  action(:begin_course_search) { |b| b.course_input_button.click}

  def search_for_a_course(course)
    course_input.set course
    begin_course_search
  end

  # Facets
  element(:seats_avail_toggle) { |b| b.li(id: "search_facet_seatsAvailable_option_seatsAvailable") }
  action(:toggle_seats_avail) { |b| b.seats_avail_toggle.click }
  element(:seats_avail_count) { |b| b.seats_avail_toggle.span(index: 1).text }
  element(:credits_toggle) { |credits, b| b.li(id: "search_facet_creditOptions_option_#{credits}") }
  action(:toggle_credits) { |credits, b| b.credits_toggle(credits).click }
  element(:course_level_toggle) { |course_level,b| b.li(id: "search_facet_courseLevel_option_#{course_level}") }
  action(:toggle_course_level) { |course_level,b| b.course_level_toggle(course_level).click }
  element(:course_prefix_toggle) { |course_prefix,b| b.li(id: "search_facet_coursePrefix_option_#{course_prefix}") }
  action(:toggle_course_prefix) { |course_prefix,b| b.course_prefix_toggle(course_prefix).click }
  element(:clear_level_facet) { |b| b.div(id: "search_facet_clear_courseLevel") }
  element(:clear_prefix_facet) { |b| b.div(id: "search_facet_clear_coursePrefix") }
  element(:clear_credit_facet) { |b| b.div(id: "search_facet_clear_creditOptions") }

  # Results
  element(:search_results_summary_div) { |b| b.div(id: "search_results_summary") }
  element(:search_results_summary) { |b| b.search_results_summary_div.text }
  element(:display_limit_select) { |b| b.select(id: "display_limit_select") }
  element(:results_table) { |b| b.table(id: "search_results_table") }
  element(:sort_selector) { |column,b| b.i(id: "sort_selector_#{column}") }
  action(:sort_results_by) { |column,b| b.sort_selector(column).click }
  element(:course_code_result_link) { |course_code,b| b.tr(id: "course_detail_row_#{course_code}").td(index: 0).span }
  element(:row_result_link) { |result_row,b| result_row.td(index: 0).span }

  # Pagination
  element(:first_page_on) { |b| b.a(id: "firstPageLink") }
  element(:first_page_off) { |b| b.span(id: "firstPage") }
  action(:first_page) { |b| b.first_page_on.click }
  element(:previous_page_on) { |b| b.a(id: "prevPageLink") }
  element(:previous_page_off) { |b| b.span(id: "prevPage") }
  action(:previous_page) { |b| b.previous_page_on.click }
  element(:next_page_on) { |b| b.a(id: "nextPageLink") }
  element(:next_page_off) { |b| b.span(id: "nextPage") }
  action(:next_page) { |b| b.next_page_on.click }
  element(:last_page_on) { |b| b.a(id: "lastPageLink") }
  element(:last_page_off) { |b| b.span(id: "lastPage") }
  action(:last_page) { |b| b.last_page_on.click }

  # Details view
  element(:back_to_search_results) { |b| b.a(id: "returnToSearch") }
  element(:course_description) { |b| b.div(id: "courseDescription").text }

  # Results table column indexes
  COURSE_CODE = 1
  COURSE_DESC = 2
  COURSE_CRED = 3

  #Gives the digit for course level comparison, eg ENGL200 would have 2 extracted and compared
  value(:courseLevel){ |row,b| row.cells[COURSE_CODE].text.slice(4,1) }

  def target_result_row_by_course(course_code)
    results_table.rows.each do |row|
      return row if row.cells[COURSE_CODE].text.match course_code
    end
    return nil
  end

  def course_title_by_course (course_code)
    row = target_result_row_by_course (course_code)
    row.cells[COURSE_DESC].text
  end

  def course_desc_link_by_course (course_code)
    row = target_result_row_by_course (course_code)
    row.cells[COURSE_DESC].link
  end

  def credits_by_course (course_code)
    row = target_result_row_by_course (course_code)
    row.cells[COURSE_CRED].text
  end

  def target_result_row_by_index(index)
    results_table.rows[index]
  end

  def course_code_by_index(index)
    row = target_result_row_by_index (index)
    row.cells[COURSE_CODE].text
  end

  def course_prefix_by_row(row)
    row.cells[COURSE_CODE].text.slice(0,4)
  end

  def course_title_by_index (index)
    row = target_result_row_by_index (index)
    row.cells[COURSE_DESC].text
  end

  def course_desc_link_by_index (index)
    row = target_result_row_by_index (index)
    row.cells[COURSE_DESC].link
  end

  def credits_by_index (index)
    row = target_result_row_by_index (index)
    row.cells[COURSE_CRED].text
  end

  def seats_avail_count_number
    seats_avail_count.match('(\d)')[1].to_i
  end

  def seats_avail
    # TODO go through all AO tables: if each table has a row with seat > 0  return true
    # issue with id search_results_table - it appears for every table present (dupe id)
  end


  def results_list

    list = []
    no_of_rows = get_results_table_rows_no(0) - 1
    for index in 0..no_of_rows do
      list << get_table_row_code(index,0)
    end
    list.delete_if { |item| item == "Code" }
    list.delete_if {|item| item == "" }
    list
  end

  def get_credit
    credit=credits.split(".")[0]
  end

  def results_list_bycolumn(columnname)
    list = []
    no_of_rows = get_results_table_rows_no(0) - 1
    for index in 0..no_of_rows do
      list << get_table_row_code(index,0)
    end
    list.delete_if { |item| item == columnname }
    list.delete_if {|item| item == "" }
    list
  end


  def results_list_gened

    list = []
    no_of_rows = get_results_table_rows_no(0) - 1
    for index in 0..no_of_rows do
      list << get_table_row_gened(index,6)
    end
    list.delete_if { |item| item == "Gen Ed" }
    list.delete_if {|item| item == "" }
    list
  end

  # Get code from data table row safely
  def get_table_row_code(index,rescues)
    begin

      code = results_table.rows[index].cells[COURSE_CODE].text
      return code
    rescue => e
      rescues = rescues+1
      puts "Retrieve code for row #{index} rescue from #{e.message}: #{rescues}"
      if rescues<5
        sleep(1)
        get_table_row_code(index,rescues)
      else
        puts "Failed to retrieve code for row #{index}"
        return ""
      end
    end
  end

  # Get title from data table row safely
  def get_table_row_title(index,rescues)
    begin
      title = results_table.rows[index].cells[COURSE_DESC].text
      return title
    rescue => e
      rescues = rescues+1
      puts "Retrieve title for row #{index} rescue from #{e.message}: #{rescues}"
      if rescues<5
        sleep(1)
        get_table_row_title(index,rescues)
      else
        puts "Failed to retrieve title for row #{index}"
        return ""
      end
    end
  end

  # Get number of data table rows safely
  def get_results_table_rows_no(rescues)
    begin
      sleep(2)
      return results_table.rows.length
    rescue => e
      rescues = rescues+1
      puts "Retrieve length rescue from #{e.message}: #{rescues}"
      if rescues<5
        return get_results_table_rows_no(rescues)
      else
        puts "Failed to retrieve length"
        return 0
      end
    end
  end


  def results_list_title
    title_list = []
    results_table.rows.each do |row|
      sleep(1)
      title_list << row[COURSE_DESC].text
    end
    title_list.delete_if { |item| item == "Code" }
    title_list.delete_if {|item| item == "" }

    puts "#{title_list}"
    sleep(2)
    title_list.sort!
    puts "#{title_list}"
  end


  def results_list_courses (expected)
    trimmed_array_list= Array.new
    results_list
    if expected.length == 4 || expected.length > 4 && expected[4] == ','
      trimmed_array_list<<results_list.map! {|x| x.slice(0,4) }
    elsif expected.length == 5
      trimmed_array_list<<results_list.map! {|x| x.slice(0,5) }
    else
      trimmed_array_list<<results_list.map! {|x| x }
    end
    trimmed_array_list
  end


  def results_list_validation(split_text,search_FullText)

    sleep(2)
    no_of_rows = results_table.rows.length-1
    #puts "No of Rows = #{no_of_rows}"

    for index in 1..no_of_rows do

      if index == no_of_rows
        sleep(2)
        course_code = results_table.rows[index].cells[COURSE_CODE].text
        sleep(1)
        course_name = results_table.rows[index].cells[COURSE_DESC].text.downcase

        puts "Course name =  #{course_name}"
        course_code_result_link(course_code).click
        sleep 2
        back_to_search_results.wait_until_present
        course_description_text = course_description(course_code).downcase


        back_to_search_results.click
        sleep(2)

        if ((course_code.downcase).include? (split_text).downcase) ||  ((course_name.include? (split_text).downcase )||(course_description_text.include? (split_text).downcase))
        else
          split_name = search_FullText.split(' ')
          falseCount = 0
          for index in 0 ... split_name.size
            split_text = "#{split_name[index]}"

            puts "split_text  = = #{split_text}"

            if ((course_code.downcase).include? (split_text).downcase) ||
                ((course_name.include? (split_text).downcase )||(course_description_text.include? (split_text).downcase))
            else
              falseCount = falseCount + 1
            end
          end

          puts "falseCount = #{falseCount}"
          puts " split size = #{split_name.size}"
          if  falseCount ==  split_name.size
            puts "#{course_code}"
            return false
          end
        end
      end
    end
  end




  def single_text_search_results_validation(single_text)
    sleep(2)
    no_of_rows = results_table.rows.length-1
    for index in 1..no_of_rows do
      if index == no_of_rows
        sleep(2)
        course_code = results_table.rows[index].cells[COURSE_CODE].text
        sleep(1)
        course_name = results_table.rows[index].cells[COURSE_DESC].text.downcase
        course_code_result_link(course_code).click
        sleep 2
        back_to_search_results.wait_until_present
        course_description_text = course_description(course_code).downcase
        back_to_search_results.click
        sleep(2)
        if ((course_code.downcase).include? (single_text).downcase) ||  ((course_name.include? (single_text).downcase )||(course_description_text.include? (single_text).downcase))
        else
          return false
        end
      end
    end
  end

#************************** Course Level Search--KSAP- 832  and US 618*********************
  def result_list_level(text)
    sleep(1)
    no_of_rows = results_table.rows.length-1
    for index in 1..no_of_rows do
      if index == no_of_rows
        sleep(2)
        course_code = results_table.rows[index].cells[COURSE_CODE].text
        puts  "courseCode1 #{course_code}"
        puts level_digit = text.slice(0)
        search_text = /(#{level_digit}\d\d)/

        sleep(2)
        sliced_course_code = course_code[4..course_code.length]
        if (search_text.match(sliced_course_code))
        else
          return false
          break
        end
      end
    end
  end

  # Validate that all returned results on current page meet search criteria
  def validate_result_list(expected_code, expected_text)
    resultList = results_list()
    no_of_results = resultList.length - 1
    for index in 0..no_of_results
      result_code = resultList[index]
      requiredFound = result_list_required_code_match(result_code,expected_code)
      if requiredFound == false
        # check for components in the course code
        found = result_list_code_match(result_code, expected_text)
        if found == false
          # if not found check against the course details full text
          found = course_details_text_match(index+1,result_code, expected_text)
        end
        if found == false
          # if not found fail
          puts "Failed on #{result_code}"
          return false
        end
      end
    end

    return true
  end

  # Determine if a specific required course was returned
  def result_list_required_code_match(result_code, expected_code)
    expectedCode = expected_code.split(",",-1)
    no_of_code = expectedCode.length
    for index in 0...no_of_code do
      code = expectedCode[index]
      formatted_code = code.downcase
      formatted_result = result_code.downcase

      if formatted_code == formatted_result
        # remove found code from expected codes
        expected_code.gsub!(code," ")
        expected_code.gsub!(" ,","")
        expected_code.gsub!(", ","")
        expected_code.gsub!(" ","")
        return true
      end
    end
    # if none are found return nil
    return false
  end

  # Determine if a search result was from a course code base search
  def result_list_code_match(result_code, expected_text)
    expectedText = expected_text.gsub("00","").gsub("xx","").gsub("XX","").split(",",-1)
    no_of_text = expectedText.length
    for index in 0...no_of_text do
      text = expectedText[index]
      size = text.length

      # Size 1 indicates an exact level so only check against the codes first digit
      if size == 1
        formatted_text = text.slice(0,size).downcase
        formatted_result = result_code.slice(4,size).downcase
      else
        # Size 3 indicates an exact code so only check against the last 3 digits
        if size == 3
          formatted_text = text.slice(0,size).downcase
          formatted_result = result_code.slice(4,size).downcase
        else
          formatted_text = text.slice(0,size).downcase
          formatted_result = result_code.slice(0,size).downcase
        end
      end

      if formatted_text == formatted_result
        return true
      end
    end
    puts "No code match on #{result_code}"
    return false
  end

  # Determine if a search result from a full text search
  def course_details_text_match (row_number, result_code, expected_text)
    expectedText = expected_text.split(",",-1)
    no_of_text = expectedText.length

    course_code = get_table_row_code(row_number,0)
    course_name = get_table_row_title(row_number,0).downcase

    course_code_result_link(result_code).click
    sleep 2
    back_to_search_results.wait_until_present
    course_description_text = course_description.downcase
    back_to_search_results.click
    sleep(2)

    for index in 0...no_of_text do
      text = expectedText[index]
      if (((result_code.downcase).include? (text).downcase) ||  (course_name.include? (text).downcase )||(course_description_text.include? (text).downcase))
        return true
      end
    end
    puts "No text match on #{result_code}"
    return false
  end


#----------------------------------------------------------------------------------------------------------------------------------------------------------
# sort_option - The values are for Ascending = true Descending = false
# code_title_option - The  values are COURSE_CODE= 0, COURSE_DESC=1
  def check_results_sort_order(sort_option,code_title_option)
    sleep(1)
    no_of_rows = results_table.rows.length-1
    puts no_of_rows
    current_code = nil
    previous_Code = nil
    for index in 1..no_of_rows do
      sleep(1)
      current_code = results_table.rows[index].cells[code_title_option].text
      sleep(1)
      if  index > 1
        begin
          if sort_option == true      #---- ASCENDING ORDER FOR CODE and TITLE
            puts "previous code #{previous_Code}"
            puts "current code #{current_code}"
            if (previous_Code <=> current_code) > 0
              return false
            end

          else                        #---DESCENDING ORDER FOR CODE and TITLE
            puts "previous code #{previous_Code}"
            puts "current code #{current_code}"
            if (previous_Code <=> current_code) < 0
              return false
            end
            previous_Code = current_code
          end
        end
      else
        return false
      end
    end
  end
end

