module CoRefData

  def engl304_published_eo_create_term
    original_co = make CourseOffering, :term => Rollover::PUBLISHED_EO_CREATE_TERM, :course => "ENGL304"

    unless original_co.exists?
      course_offering = make CourseOffering, :term=> original_co.term,
                             :course => original_co.course,
                             :suffix => ' ',
                             :final_exam_driver => "Final Exam Per Activity Offering"
      course_offering.delivery_format_list[0].format = "Lecture/Discussion"
      course_offering.delivery_format_list[0].grade_format = "Discussion"
      course_offering.delivery_format_list[0].final_exam_activity = "Lecture"
      course_offering.create

      activity_offering = create ActivityOfferingObject, :parent_course_offering => course_offering,
                                 :format => "Lecture/Discussion", :activity_type => "Lecture"
      si_obj =  make SchedulingInformationObject, :days => "TH",
                     :start_time => "11:00", :start_time_ampm => "am",
                     :end_time => "11:50", :end_time_ampm => "am",
                     :facility => 'TWS', :room => '1100'
      activity_offering.add_req_sched_info :rsi_obj => si_obj, :defer_save => true
      activity_offering.edit :start_edit => false,
                             :send_to_scheduler => true

      #TODO: KSENROLL-13157 problems creating 2nd AO
      # activity_offering = create ActivityOfferingObject, :parent_course_offering => course_offering,
      #                            :format => "Lecture/Discussion", :activity_type => "Discussion"
      # si_obj =  make SchedulingInformationObject, :days => "W",
      #                :start_time => "09:00", :start_time_ampm => "am",
      #                :end_time => "09:50", :end_time_ampm => "am",
      #                :facility => 'KEY', :room => '0117'
      # activity_offering.add_req_sched_info :rsi_obj => si_obj
    end
    original_co
  end

  def engl301_published_eo_create_term

    original_co = make CourseOffering, :term => Rollover::PUBLISHED_EO_CREATE_TERM, :course => "ENGL301"

    unless original_co.exists?
      course_offering = make CourseOffering, :term=> original_co.term,
                             :course => original_co.course,
                             :suffix => ' ',
                             :final_exam_driver => "Final Exam Per Activity Offering"
      course_offering.delivery_format_list[0].format = "Lecture"
      course_offering.delivery_format_list[0].grade_format = "Lecture"
      course_offering.delivery_format_list[0].final_exam_activity = "Lecture"
      course_offering.create

      activity_offering = create ActivityOfferingObject, :parent_course_offering => course_offering,
                                 :format => "Lecture Only", :activity_type => "Lecture"
      si_obj =  make SchedulingInformationObject, :days => "MW",
                     :start_time => "11:00", :start_time_ampm => "am",
                     :end_time => "12:15", :end_time_ampm => "pm",
                     :facility => 'TWS', :room => '3132'
      activity_offering.add_req_sched_info :rsi_obj => si_obj, :defer_save => true
      activity_offering.edit :start_edit => false,
                             :send_to_scheduler => true

      #TODO: KSENROLL-13157 problems creating 2nd AO
      # activity_offering = create ActivityOfferingObject, :parent_course_offering => course_offering,
      #                            :format => "Lecture/Discussion", :activity_type => "Discussion"
      # si_obj =  make SchedulingInformationObject, :days => "W",
      #                :start_time => "09:00", :start_time_ampm => "am",
      #                :end_time => "09:50", :end_time_ampm => "am",
      #                :facility => 'KEY', :room => '0117'
      # activity_offering.add_req_sched_info :rsi_obj => si_obj
    end
    original_co
  end

  def engl305_published_eo_create_term
    original_co = make CourseOffering, :term => Rollover::PUBLISHED_EO_CREATE_TERM, :course => "ENGL305"

    unless original_co.exists?
      course_offering = make CourseOffering, :term=> @original_co.term,
                             :course => @original_co.course,
                             :suffix => ' ',
                             :final_exam_driver => "Final Exam Per Activity Offering"
      course_offering.delivery_format_list[0].format = "Lecture/Discussion"
      course_offering.delivery_format_list[0].grade_format = "Discussion"
      course_offering.delivery_format_list[0].final_exam_activity = "Lecture"
      course_offering.create

      activity_offering = create ActivityOfferingObject, :parent_course_offering => course_offering,
                                 :format => "Lecture/Discussion", :activity_type => "Lecture"
      si_obj =  make SchedulingInformationObject, :days => "TH",
                     :start_time => "12:30", :start_time_ampm => "pm",
                     :end_time => "01:45", :end_time_ampm => "pm",
                     :facility => 'TWS', :room => '1106'
      activity_offering.add_req_sched_info :rsi_obj => si_obj, :defer_save => true
      activity_offering.edit :start_edit => false,
                             :send_to_scheduler => true

      #TODO: KSENROLL-13157 problems creating 2nd AO
      # activity_offering = create ActivityOfferingObject, :parent_course_offering => course_offering,
      #                            :format => "Lecture/Discussion", :activity_type => "Discussion"
      # si_obj =  make SchedulingInformationObject, :days => "W",
      #                :start_time => "09:00", :start_time_ampm => "am",
      #                :end_time => "09:50", :end_time_ampm => "am",
      #                :facility => 'KEY', :room => '0117'
      # activity_offering.add_req_sched_info :rsi_obj => si_obj
    end
    original_co
  end

  def engl201_published_eo_create_term
    original_co =  make CourseOffering, :term => Rollover::PUBLISHED_EO_CREATE_TERM, :course => "ENGL201"

    unless original_co.exists?
      course_offering = make CourseOffering, :term=> original_co.term,
                             :course => original_co.course,
                             :suffix => ' ',
                             :final_exam_driver => "Final Exam Per Activity Offering"
      course_offering.delivery_format_list[0].format = "Lecture"
      course_offering.delivery_format_list[0].grade_format = "Lecture"
      course_offering.delivery_format_list[0].final_exam_activity = "Lecture"
      course_offering.create

      activity_offering = create ActivityOfferingObject, :parent_course_offering => course_offering,
                                 :format => "Lecture Only", :activity_type => "Lecture"
      si_obj =  make SchedulingInformationObject, :days => "TH",
                     :start_time => "11:00", :start_time_ampm => "am",
                     :end_time => "12:15", :end_time_ampm => "pm",
                     :facility => 'SQH', :room => '1101'
      activity_offering.add_req_sched_info :rsi_obj => si_obj, :defer_save => true
      activity_offering.edit :start_edit => false,
                             :send_to_scheduler => true

      #TODO: KSENROLL-13157 problems creating 2nd AO
      # activity_offering = create ActivityOfferingObject, :parent_course_offering => course_offering,
      #                            :format => "Lecture/Discussion", :activity_type => "Discussion"
      # si_obj =  make SchedulingInformationObject, :days => "W",
      #                :start_time => "09:00", :start_time_ampm => "am",
      #                :end_time => "09:50", :end_time_ampm => "am",
      #                :facility => 'KEY', :room => '0117'
      # activity_offering.add_req_sched_info :rsi_obj => si_obj
    end
    original_co
  end

end #module