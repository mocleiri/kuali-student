Given /^the SOC is valid for "(.*?)"$/ do |currentState|
  @manageSoc = make ManageSoc
  @manageSoc.search
  @manageSoc.check_state_change_button_exists currentState
end

When /^I "(.*?)" the SOC and press "(.*?)" on the confirm dialog$/ do |newState,confirmStateChange|
  @manageSoc.change_action newState, confirmStateChange
end

Then /^I verify that "(.*?)" button is there for next action$/ do |nextState|
  @manageSoc.check_state_change_button_exists nextState
end