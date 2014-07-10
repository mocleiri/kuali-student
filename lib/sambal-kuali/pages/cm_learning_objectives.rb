class CmLearningObjectives < BasePage

  wrapper_elements
  cm_elements # for loading

  # ['Student Oversight Unit', 'Deployment Division', 'Financial Control Unit', 'Curriculum Oversight Unit', 'Student Oversight Division',
  # 'Financial Resources Division', 'Curriculum Oversight Division', 'Financial Resources Unit', 'Deployment Unit',
  # 'Administering Org', 'Financial Control Division', 'Curriculum Oversight', 'Institution']

  action(:add_learning_objective) { |b| b.button(id: "LearningObjective-ToolBar-AddNewObjective").click; b.loading_wait }
  action(:find_learning_objective) { |b| b.button(id: "KS-LearningObjective-QuickFinder_act").click }

  action(:objective_detail) { |objective_level, b| b.text_field(id: "KS-LoDisplayInfoWrapper-descr_line#{objective_level-1}_control") }
  action(:search_for_lo) { |objective_level,b| b.a(id: "KS-LoDisplayInfoWrapper-descr_line#{objective_level-1}_quickfinder_act") }
  action(:delete_learning_objective) { |objective_level, b| b.a(id: "LearningObjectives-Icon-Delete_line#{objective_level}").click ; b.loading_wait }

#  action(:category_detail) { |category_level,b| b.text_field(id: "KS-LearningObjective-Category_line#{category_level-1}_add_control") }
  action(:category_detail) { |objective_level,category_level,b| b.div(id: "learning_objective_section_#{objective_level-1}").text_field(id: "KS-LearningObjective-Category_line#{category_level-1}_add_control") }
  action(:category_type) { |objective_level,category_level, b| b.div(id: "learning_objective_section_#{objective_level-1}").select_list(id: "KS-LearningObjective-CategoryType_line#{category_level-1}_add_control") }
  action(:find_categories) { |category_level, b| b.a(id: "KS-LearningObjective-Category-QuickFinder_line#{category_level-1}_act").click; b.loading_wait }
  action(:add_category) { |objective_level,category_level,b| b.div(id: "learning_objective_section_#{objective_level-1}").button(id: "KS-LearningObjective-Category-Add_line#{category_level-1}_add").click; b.loading_wait }

  action(:category_detail_added) { |objective_level,category_level,b| b.text_field(id: "KS-LearningObjective-Category_line#{objective_level-1}_line#{category_level-1}" ) }
  action(:delete_category) { |objective_level,category_level,b| b.a(id: "KS-LearningObjective-Category-Delete_line#{objective_level-1}_line#{category_level-1}").i(class: "ks-fontello-icon-cancel").click; b.loading_wait }

  #organize LO
  action(:move_up_lo) { |lo_level,b| b.a(id: "LearningObjectives-Icon-MoveUp_line#{lo_level-1}").click; b.loading_wait }
  action(:move_down_lo) { |lo_level,b| b.a(id: "LearningObjectives-Icon-MoveDown_line#{lo_level-1}").click; b.loading_wait }
  action(:outdent_lo) { |lo_level,b| b.a(id: "LearningObjectives-Icon-DecreaseIndent_line#{lo_level-1}").click; b.loading_wait }
  action(:indent_lo) { |lo_level,b| b.a(id: "LearningObjectives-Icon-IncreaseIndent_line#{lo_level-1}").click; b.loading_wait }

end
