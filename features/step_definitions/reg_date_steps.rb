And /^I attempt to register for (a )?courses? for Half Fall (\d+) (\d+)$/ do |arg1, termHalf, year|
  term_descr = "Fall #{year}"
  course_code = ""
  case year
    when 2012 then
      course_code = case termHalf
                      when 1 then "CHEM105"
                      when 2 then "CHEM147"
                    end
  end
  @reg_request = make RegistrationRequest,
                      :term_descr=>term_descr,
                      :course_code=>course_code,
                      :reg_group_code=>"1001"
  @reg_request.create
  @reg_request.register

  end

Then /^there is a message indicating that the registration period is not open$/ do
  on RegistrationCart do |page|
    page.course_code_message(@reg_request.course_code,@reg_request.reg_group_code).text.should =~ /not open/i
  end
end
