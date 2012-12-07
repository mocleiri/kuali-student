class ActivityOfferingCluster

  include Foundry
  include DataFactory
  include DateFactory
  include StringFactory
  include Workflows

  attr_accessor :is_constrained,
                :private_name,
                :published_name,
                :is_valid,
                :expected_msg,
                :to_assign_ao_list,
                :assigned_ao_list

  alias_method :constrained?, :is_constrained
  alias_method :valid?, :is_valid

  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :is_constrained=>true,
        :private_name=>"#{random_alphanums(5).strip}_pri",
        :published_name=>"#{random_alphanums(5).strip}_pub",
        :is_valid=>true,
        :expected_msg=>"",
        :assigned_ao_list=>[],
        :to_assign_ao_list=>["A"]
    }
    options = defaults.merge(opts)
    set_options(options)
  end

  def create_cluster(opts={})
    defaults = {
        :is_constrained=>@is_constrained,
        :private_name=>@private_name,
        :published_name=>@published_name,
        :is_valid=>@is_valid,
        :expected_msg=>@expected_msg,
        :assigned_ao_list=>@assigned_ao_list,
        :to_assign_ao_list=>@to_assign_ao_list
    }
    options = defaults.merge(opts)
    set_options(options)

    if @is_constrained
      on ManageRegistrationGroups do |page|
        page.create_new_cluster
        page.private_name.set @private_name
        page.published_name.set @published_name
        page.create_cluster
      end
    else
      @private_name = "Default Cluster"
      @public_name = @private_name
    end

  end

  def add_unassigned_aos(ao_list=[])
    @to_assign_ao_list = ao_list unless ao_list == []

    puts "@to_assign_ao_list: #{@to_assign_ao_list}"

    on ManageRegistrationGroups do |page|
      @to_assign_ao_list.each do |ao_code|
        page.select_unassigned_ao_row(ao_code)
        @assigned_ao_list << ao_code
      end
      page.ao_cluster_select.select @private_name
      if @to_assign_ao_list.length != 0
        page.ao_cluster_assign_button
        while true
          begin
            sleep 1
            wait_until { page.get_cluster_ao_row(@private_name, @to_assign_ao_list[0]).exists? }
            break
          rescue Selenium::WebDriver::Error::StaleElementReferenceError
            puts "rescued"
          end
        end
      end
      @to_assign_ao_list = []
    end
  end

  def generate_unconstrained_reg_groups
    @to_assign_ao_list = []
    on ManageRegistrationGroups do |page|
      page.generate_unconstrained_reg_groups
      while true
        begin
          sleep 1
          wait_until(10) { page.cluster_list_div.exists? }
          #wait_until { page.cluster_list_item_div(@private_name).exists? }
          break
        rescue Watir::Wait::TimeoutError #in case generation fails
          break
        rescue Selenium::WebDriver::Error::StaleElementReferenceError
          puts "rescued - generate_unconstrained_reg_groups"
        end
      end
    end
  end

  def generate_reg_groups
    on ManageRegistrationGroups do |page|
      page.cluster_generate_reg_groups(@private_name)
    end
  end

end


=begin
puts page.ao_table.rows.count
puts page.cluster_list_row_name_text("test1pri")
page.ao_cluster_select.select("test1")
page.cluster_list_row_generate_reg_groups("test1")
puts page.target_ao_row("A").cells[1].text
puts page.target_ao_row("A").cells[2].text
page.select_ao_row("A")
page.ao_cluster_select.select("test1pub")
page.ao_cluster_assign_button
=end
