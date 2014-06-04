class CmActiveDates < BasePage

  wrapper_elements
  cm_elements

  # Example of options 'Spring 1980', 'Fall 1985'
  element(:start_term) {|b| b.select_list(name: 'document.newMaintainableObject.dataObject.courseInfo.startTerm') }
  element(:pilot_course) { |b| b.checkbox(id: "CourseView-ActiveDatesPage-PilotCourse_control") }
  action(:set_pilot_course) { |b| b.pilot_course.set; b.loading_wait }
  element(:end_term) { |b| b.select_list(id: "CourseView-ActiveDatesPage-EndTerm_control") }



end
