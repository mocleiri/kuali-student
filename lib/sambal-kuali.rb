require 'test-factory'

$: << File.dirname(__FILE__)+'/sambal-kuali'
require 'sambal-kuali/kuali_base_page'
require 'sambal-kuali/data_objects/ks_data_object'
Dir["#{File.dirname(__FILE__)}/sambal-kuali/*.rb"].each {|f| require f }
#Dir["#{File.dirname(__FILE__)}/sambal-kuali/pages/*.rb"].each {|f| require f }
Dir["#{File.dirname(__FILE__)}/sambal-kuali/pages/**/*.rb"].each {|f| require f }
Dir["#{File.dirname(__FILE__)}/sambal-kuali/data_objects/**/*.rb"].each {|f| require f }
Dir["#{File.dirname(__FILE__)}/test_factory_ext/**/*.rb"].each {|f| require f }



