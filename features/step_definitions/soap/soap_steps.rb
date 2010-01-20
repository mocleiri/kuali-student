Given /^(?:|I )am connected to the "([^\"]*)" service$/ do |service_name|
  @client = Savon::Client.new soap_path_to(service_name)
end

When /^(?:|I )call the "([^\"]*)" action with params "([^\"]*)"$/ do |action_name, params|
  @response = @client.instance_eval(action_name + build_action_params(params))
end

Then /^response parameter "([^\"]*)" for the action "([^\"]*)" should contain "([^\"]*)"$/ do |response_parameter, action_name, value|
  puts @response.to_hash[action_name.to_sym][response_parameter.to_sym].should == value
end


Then /^show me the output in the console$/ do
  puts @response.to_hash.inspect
end


def build_action_params(params)
  "{|soap| soap.body={#{params}}}"
end