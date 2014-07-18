class AdminComments < BasePage

  expected_element :add_comment

  def frm
    self.iframe(class: "fancybox-iframe")
  end


end