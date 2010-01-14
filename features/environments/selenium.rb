Webrat.configure do |config|
  config.mode = :selenium
  config.application_framework = :external
end
class ActiveSupport::TestCase
  setup do |session|
    session.host! "localhost:8080"
  end
end