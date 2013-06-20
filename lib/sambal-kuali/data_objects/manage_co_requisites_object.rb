# Created data used for testing
#
# class attributes are initialized with default data unless values are explicitly provided
#
# Typical usage: (with optional setting of explicit data value in  )
#  @manageCOR = make ManageCORequisitesData
#  @editAgenda.create_data_advanced_search(section, course)
# Methods:
#  @advanced_search(field, code)
#  @check_data_existence
#  @check_new_data_existence
#  @create_data_advanced_search( sect)
#  @edit_data_advanced_search( sect)
#  @create_course_rule( course, sect)
#  @create_text_rule( text)
#  @create_all_courses_rule( courses, sect)
#  @create_number_courses_rule( courses, sect)
#  @edit_existing_node(node, field, code)
#  @add_new_node(field, node, code)
#  @create_new_group(sect, field, node, course, set)
#  @switch_tabs
#  @check_text_correct( text, correct, section)
#  @convert_text( text, page)
#  @test_text(section, text, boolean)
#  @test_multi_line_text( section, test_string, boolean)
#  @test_single_line_text( section, test_text, boolean)
#  @test_popup_text( text, boolean)
#  @change_operator(level, operator)
#  @test_node_level( level, node)
#  @move_around( node, direction)
#  @copy_cut_paste( node, node_after, action)
#
# Note the use of the ruby options hash pattern re: setting attribute values

class ManageCORequisitesData
  include Foundry
  include DataFactory

  attr_accessor :select_rule

  def initialize(browser, opts={})
    @browser = browser

    defaults = {
    }

    options = defaults.merge(opts)

    set_options(options)
  end

  def advanced_search(field, code)
    fields = {"course code"=>:lookup_course_code, "course title"=>:lookup_course_title, "course set"=>:lookup_set_name,
              "course range"=>"????", }  ##############################################
    on ManageCORequisites do |page|
      page.edit_loading.wait_while_present
      page.search_link
      page.send(fields[field]).when_present.set code
      page.lookup_search_button
      page.loading.wait_while_present
      if field == "course code" || field == "course set"#####################################
        page.lookup_results.a(:title => /.*#{Regexp.escape(code)}.*/).click
      else
        #TODO - find a way to return course code randomly when searching for title or description
      end
    end
    #sleep 3 #adding sleep, moving through the tree too fast causes test to crash
  end

  def check_data_existence
    on CourseOfferingRequisites do |page|
      page.loading.wait_while_present
      if page.rule_edit_links.exists?
        return 1
      else
        return 0
      end
    end
  end

  def check_new_data_existence
    on ManageCORequisites do |page|
      page.logic_tab.click
      page.edit_loading.wait_while_present
      text = page.logic_text.text
      array = text.split('(')
      page.object_tab.click
      page.edit_loading.wait_while_present
      if array.length >= 3
        return 1
      else
        return 0
      end
    end
  end

  def create_data_advanced_search( sect)
    on ManageCORequisites do |page|
      create_course_rule( "add", "", "ENGL101", sect)
      create_course_rule( "add", "", "HIST639", sect)
      create_text_rule( "add", "", "free form text input value")
      create_all_courses_rule( "group", "A", "ENGL478,HIST416", "", "", sect)
      create_text_rule( "add", "", "Text")
      create_text_rule( "group", "F", "Text to copy")
      create_number_courses_rule( "add", "D", "1", "HIST395,HIST210", "", "", sect)
      page.loading.wait_while_present
      page.edit_tree_section.select(:id => /u\d+_node_\d+_parent_node_0_parent_root_control/).when_present.select "OR"
      page.edit_loading.wait_while_present
      page.edit_tree_section.select(:id => /u\d+_node_\d+_parent_node_\d+_parent_node_\d+_parent_node_0_parent_root_control/).when_present.select "OR"
      page.edit_loading.wait_while_present
    end
  end

  def edit_data_advanced_search( sect)
    on ManageCORequisites do |page|
      create_course_rule( "add", "C", "ENGL101", sect)
      create_text_rule( "group", "B", "free form text input value")
      create_all_courses_rule( "add", "", "ENGL478,HIST416", "", "", sect)
      create_text_rule( "group", "F", "Text")
      create_number_courses_rule( "add", "D", "1", "HIST395,HIST210", "", "", sect)
      page.loading.wait_while_present
      page.edit_tree_section.select(:id => /u\d+_node_\d+_parent_node_0_parent_root_control/).when_present.select "AND"
      page.edit_loading.wait_while_present
      page.edit_tree_section.select(:id => /u\d+_node_\d+_parent_node_\d+_parent_node_0_parent_root_control/).when_present.select "OR"
      page.edit_loading.wait_while_present
    end
  end

  def create_course_rule( group, node, course, sect)
    add_new_node( group, node)
    on ManageCORequisites do |page|
      if( sect == "Student Eligibility & Prerequisite" || sect == "Recommended Preparation")
        page.rule_dropdown.when_present.select /Must have successfully completed <course>/
      elsif( sect == "Antirequisite" || sect == "Course that Restricts Credits")
        page.rule_dropdown.when_present.select /Must not have successfully completed <course>/
      elsif( sect == "Corequisite")
        page.rule_dropdown.when_present.select /Must be concurrently enrolled in <course>/
      elsif( sect == "Repeatable for Credit")
        page.rule_dropdown.when_present.select /May not repeat <course>/
      end
      advanced_search("course code", course)
      page.preview_btn
    end
  end

  def create_text_rule( group, node, text)
    add_new_node( group, node)
    on ManageCORequisites do |page|
      page.rule_dropdown.when_present.select /Free Form Text/
      page.free_text_field.when_present.set text
      page.preview_btn
    end
  end

  def create_all_courses_rule( group, node, course, set, range, sect)
    add_new_node( group, node)
    on ManageCORequisites do |page|
      if( sect == "Student Eligibility & Prerequisite" || sect == "Recommended Preparation")
        page.rule_dropdown.when_present.select /Must have successfully completed all courses from <courses>/
      elsif( sect == "Antirequisite" || sect == "Course that Restricts Credits")
        page.rule_dropdown.when_present.select /Must not have successfully completed any courses from <courses>/
      elsif( sect == "Corequisite")
        page.rule_dropdown.when_present.select /Must be concurrently enrolled in all courses from <courses>/
      end
      add_courses( course, set, range)
      page.preview_btn
    end
  end

  def create_number_courses_rule( group, node, number, course, set, range, sect)
    add_new_node( group, node)
    on ManageCORequisites do |page|
      if( sect == "Student Eligibility & Prerequisite" || sect == "Recommended Preparation")
        page.rule_dropdown.when_present.select /Must have successfully completed a minimum of <n> courses from <courses>/
        page.integer_field.when_present.set number
      elsif( sect == "Corequisite")
        page.rule_dropdown.when_present.select /Must be concurrently enrolled in a minimum of <n> courses from <courses>/
        page.integer_field.when_present.set number
      end
      add_courses( course, set, range)
      page.preview_btn
    end
  end

  def create_repeated_credit_rule( group, node, number)
    add_new_node( group, node)
    on ManageCORequisites do |page|
      page.rule_dropdown.when_present.select /May be repeated for a maximum of <n> credits/
      page.integer_field.when_present.set number
      page.preview_btn
    end
  end

  def create_grade_courses_rule( group, node, course, set, range, type, grade, sect)
    add_new_node( group, node)
    on ManageCORequisites do |page|
      if( sect == "Student Eligibility & Prerequisite" || sect == "Recommended Preparation")
        page.rule_dropdown.when_present.select /Must have earned a minimum grade of <gradeType> <grade> in <courses>/
      elsif( sect == "Antirequisite")
        page.rule_dropdown.when_present.select /Must not have earned a grade of <gradeType> <grade> or higher in <courses>/
      end
      page.grade_fieldset.label(:text => /#{Regexp.escape(type)}/).when_present.click
      page.grade_dropdown.when_present.select grade
      add_courses( course, set, range)
      page.preview_btn
    end
  end

  def create_grade_number_courses_rule( group, node, course, set, range, type, grade, number)
    add_new_node( group, node)
    on ManageCORequisites do |page|
      page.rule_dropdown.when_present.select /Must successfully complete a minimum of <n> courses from <courses> with a minimum grade of <gradeType> <grade>/
      page.integer_field.when_present.set number
      page.grade_fieldset.label(:text => /#{Regexp.escape(type)}/).when_present.click
      page.grade_dropdown.when_present.select grade
      add_courses( course, set, range)
      page.preview_btn
    end
  end

  def create_gpa_courses_rule( group, node, course, set, range, gpa)
    add_new_node( group, node)
    on ManageCORequisites do |page|
      page.rule_dropdown.when_present.select /Must have earned a minimum GPA of <GPA> in <courses>/
      page.integer_field.when_present.set gpa
      add_courses( course, set, range)
      page.preview_btn
    end
  end

  def create_gpa_duration_rule( group, node, gpa, type, duration)
    add_new_node( group, node)
    on ManageCORequisites do |page|
      page.rule_dropdown.when_present.select /Must have earned a minimum Cumulative GPA of <GPA> in <duration><durationType>/
      page.integer_field.when_present.set gpa
      page.duration_field.when_present.set duration
      page.duration_dropdown.when_present.select type
      page.preview_btn
    end
  end

  def create_program_rule( group, node, program, sect)
    add_new_node( group, node)
    on ManageCORequisites do |page|
      if( sect == "Student Eligibility & Prerequisite" || sect == "Recommended Preparation")
        page.rule_dropdown.when_present.select /Must have been admitted to the <Program> Program/
      elsif( sect == "Antirequisite")
        page.rule_dropdown.when_present.select /Must not have been admitted to the <Program> Program/
      end
      page.program_dropdown.when_present.select /Approved Programs/
      add_program( program)
      page.preview_btn
    end
  end

  def add_courses( course, set, range)
    on ManageCORequisites do |page|
      courses = create_array( course)
      if courses != "" && courses != nil
        page.multi_course_dropdown.when_present.select /Approved Courses/
        if courses.length > 1
          courses.each do |elem|
            advanced_search("course code", elem)
            page.add_line_btn
            page.adding.wait_while_present
          end
        else
          advanced_search("course code", course)
          page.add_line_btn
          page.adding.wait_while_present
        end
      end
      sets = create_array( set)
      if sets != "" && sets != nil
        page.multi_course_dropdown.when_present.select /Course Sets/
        if sets.length > 1
          sets.each do |elem|
            advanced_search("course set", elem)
            page.add_line_btn
            page.adding.wait_while_present
          end
        else
          advanced_search("course set", set)
          page.add_line_btn
          page.adding.wait_while_present
        end
      end
      #if range != "" && range != nil         ###FIX##################################
      #  page.multi_course_dropdown.when_present.select /Course Ranges/
      #  advanced_search("course range", range)
      #  page.add_line_btn
      #  page.adding.wait_while_present
      #end                              ######################################
    end
  end

  def add_program( program)
    on ManageCORequisites do |page|
      programs = create_array( program)
      if programs != "" && programs != nil
        page.program_dropdown.when_present.select /Approved Programs/
        programs.each do |elem|
          advanced_search("course code", elem)
          page.add_line_btn
          page.adding.wait_while_present
        end
      end
    end
  end

  def edit_existing_node(node, field, code)
    on ManageCORequisites do |page|
      page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).when_present.click
      page.edit_btn
      if field == "course"
        advanced_search("course code", code)
      elsif field == "courses"
        page.multi_course_dropdown.when_present.select /Approved Courses/
        advanced_search("course code", code)
        page.add_line_btn
        page.adding.wait_while_present
      elsif field == "text"
        page.free_text_field.when_present.set code
      end
      page.preview_btn
    end
  end

  def add_new_node( group, node)
    on ManageCORequisites do |page|
      if node != "" && node != nil && page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).exists?
        page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).when_present.click
      end
      if group == "group"
        page.group_btn
      else
        page.add_btn
      end
    end
  end

  #def add_new_gradetype_node(sect, field, node, course, set, range, type, grade)
  #  on ManageCORequisites do |page|
  #    if node != "" && node != nil && page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).exists?
  #      page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).when_present.click
  #    end
  #    page.add_btn
  #    courses = create_array(course)
  #    sets = create_array(set)
  #    if field == "courses"
  #      create_grade_courses_rule( courses, sets, range, type, grade, sect)
  #    elsif field == "number of courses"
  #      create_grade_number_courses_rule( courses, sets, range, type, grade, sect)
  #    end
  #  end
  #end

  def create_array( string)
    if string != "" && string != nil
      strings = string.split(/,/)
    else
      strings = string
    end
    return strings
  end

  #def create_new_group(sect, field, node, course, set, range)
  #  on ManageCORequisites do |page|
  #    page.loading.wait_while_present
  #    if node != "" && node != nil && page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).exists?
  #      page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).when_present.click
  #      page.group_btn
  #      if field == "course"
  #        create_course_rule( course, sect)
  #      elsif field == "courses"
  #        courses = course.split(/,/)
  #        create_all_courses_rule( courses, set, range, sect)
  #      elsif field == "text"
  #        create_text_rule( course)
  #      elsif field == "number of courses"
  #        courses = course.split(/,/)
  #        create_number_courses_rule( courses, set, range, sect)
  #      end
  #    end
  #  end
  #end

  def switch_tabs
    on ManageCORequisites do |page|
      page.edit_loading.wait_while_present
      tab = page.tab_section.li(:class => /ui-state-active/).text
      if tab == "Edit Rule"
        page.logic_tab.when_present.click
      else
        page.object_tab.when_present.click
      end
    end
  end

  def check_text_correct( text, correct, section)
    @courseORdata = make CORequisitesData, :section => section
    on ManageCORequisites do |page|
      test_text("edit", convert_text( text, "edit"), correct)
      page.logic_tab.click
      page.edit_loading.wait_while_present
      test_text("logic", convert_text( text, "logic"), correct)
      page.update_rule_btn
    end

    on CourseOfferingRequisites do |page|
      page.loading.wait_while_present
      test_text("agenda", convert_text( text, "agenda"), correct)
    end

    @courseORdata.commit_changes( true)

    on CourseOfferingRequisites do |page|
      page.loading.wait_while_present
      test_text("agenda", convert_text( text, "agenda"), correct)
      page.rule_edit
    end

    on ManageCORequisites do |page|
      page.loading.wait_while_present
      test_text("edit", convert_text( text, "edit"), correct)
      page.logic_tab.click
      page.edit_loading.wait_while_present
      test_text("logic", convert_text( text, "logic"), correct)
    end
  end

  def convert_text( text, page)
    if page == "agenda" || page == "logic"
      if text =~ /^(.+)\((.+)\)$/
        converted = $1
        array = $2.split(/, /)
        array.each do |elem|
          converted += "," + elem
        end
      else
        converted = text
      end
    else
      converted = text
    end
    return converted
  end

  def test_text( section, text, boolean)
    status = false
    string = ".*"
    if section == "edit" || text !~ /,/
      test_single_line_text( section, text, boolean)
      status = true
    else
      array = text.split(/,/)
      array.each do |elem|
        if section == "compare"
          test_popup_text( elem, boolean)
          status = true
        else
          string += elem + ".*"
          status = false
        end
      end
    end
    if status == false
      test_multi_line_text( section, Regexp.new(string, Regexp::MULTILINE), boolean)
    end
  end

  def test_multi_line_text( section, test_string, boolean)
    if section == "agenda"
      on CourseOfferingRequisites do |page|
        page.loading.wait_while_present
        if boolean == true
          page.agenda_management_section.text.should =~ test_string
        else
          page.agenda_management_section.text.should_not =~ test_string
        end
      end
    else
      sect = {"edit"=>:edit_tree_section, "logic"=>:preview_tree_section}
      on ManageCORequisites do |page|
        page.loading.wait_while_present
        if boolean == true
          page.send(sect[section]).text.should =~ test_string
        else
          page.send(sect[section]).text.should_not =~ test_string
        end
      end
    end
  end

  def test_single_line_text( section, test_text, boolean)
    if section == "agenda"
      on CourseOfferingRequisites do |page|
        page.loading.wait_while_present
        if boolean == true
          page.agenda_management_section.text.should =~ /.*#{Regexp.escape(test_text)}.*/m
        else
          page.agenda_management_section.text.should_not =~ /.*#{Regexp.escape(test_text)}.*/m
        end
      end
    elsif section == "compare"
      test_popup_text( test_text, boolean)
    else
      sect = {"edit"=>:edit_tree_section, "logic"=>:preview_tree_section}
      on ManageCORequisites do |page|
        page.loading.wait_while_present
        if boolean == true
          page.send(sect[section]).text.should =~ /.*#{Regexp.escape(test_text)}.*/m
        else
          page.send(sect[section]).text.should_not =~ /.*#{Regexp.escape(test_text)}.*/m
        end
      end
    end
  end

  def test_popup_text( text, boolean)
    on CourseOfferingRequisites do |page|
      page.loading.wait_while_present
      if boolean == true
        page.compare_tree.text.should =~ /.*#{Regexp.escape(text)}\n#{Regexp.escape(text)}.*/m
      else
        page.compare_tree.text.should_not =~ /.*#{Regexp.escape(text)}\n#{Regexp.escape(text)}.*/m
      end
    end
  end

  def change_operator(level, operator)
    on ManageCORequisites do |page|
      page.loading.wait_while_present
      if level == "primary"
        page.edit_tree_section.select(:id => /u\d+_node_\d_parent_node_0_parent_root_control/).when_present.select operator
      elsif level == "secondary"
        page.edit_tree_section.select(:id => /u\d+_node_\d_parent_node_\d_parent_node_0_parent_root_control/).when_present.select operator
      elsif level == "tertiary"
        page.edit_tree_section.select(:id => /u\d+_node_\d_parent_node_\d_parent_node_\d_parent_node_0_parent_root_control/).when_present.select operator
      end
    end
  end

  def test_node_level( level, node)
    on ManageCORequisites do |page|
      page.loading.wait_while_present
      if level == "primary"
        page.edit_tree_section.span(:text => /.*#{node}\..*/).id.should =~ /u\d+_node_\d_parent_node_0_parent_root_span/
      elsif level == "secondary"
        page.edit_tree_section.span(:text => /.*#{node}\..*/).id.should =~ /u\d+_node_\d_parent_node_\d_parent_node_0_parent_root_span/
      elsif level == "tertiary"
        page.edit_tree_section.span(:text => /.*#{node}\..*/).id.should =~ /u\d+_node_\d_parent_node_\d_parent_node_\d_parent_node_0_parent_root_span/
      end

    end
  end

  def move_around( node, direction)
    on ManageCORequisites do |page|
      if page.edit_tree_section.html =~ /li.+class.+ruleBlockSelected/
        selection = page.edit_tree_section.li(:class => /ruleBlockSelected/).text
        if selection !~ /.*#{Regexp.escape(node)}\..*/
          page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).when_present.click
        end
      else
        page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).when_present.click
      end
      if direction == "down"
        page.down_btn
      elsif direction == "up"
        page.up_btn
      elsif direction == "out"
        page.left_btn
      elsif direction == "in"
        page.right_btn
      elsif direction == "out in"
        page.left_btn
        page.right_btn
      elsif direction == "out up in"
        page.left_btn
        page.up_btn
        page.right_btn
      end
    end
  end

  def copy_cut_paste( node, node_after, action)
    on ManageCORequisites do |page|
      if node != "" && node != nil && page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).exists?
        page.edit_tree_section.span(:text => /.*#{Regexp.escape(node)}\..*/).when_present.click
      end
      if action == "copy"
        page.copy_btn
      elsif action == "cut"
        page.cut_btn
      end
      if node_after != "" && node_after != nil && page.edit_tree_section.span(:text => /.*#{Regexp.escape(node_after)}\..*/).exists?
        page.edit_tree_section.span(:text => /.*#{Regexp.escape(node_after)}\..*/).when_present.click
      end
      page.paste_btn
    end
  end
end