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
        :to_assign_ao_list=>["C"]
    }
    options = defaults.merge(opts)
    set_options(options)
  end

  def create_cluster
    on ManageRegistrationGroups do |page|
      page.create_new_cluster
      page.private_name.set @private_name
      page.published_name.set @published_name
      page.create_cluster
    end
  end

  def add_unassigned_aos
    on ManageRegistrationGroups do |page|
      @to_assign_ao_list.each do |ao_code|
        page.select_unassigned_ao_row(ao_code)
        @assigned_ao_list << ao_code
      end
      page.ao_cluster_select.select @private_name
      page.ao_cluster_assign_button unless @to_assign_ao_list.length == 0
      @to_assign_ao_list = []
    end
  end

  def generate_unconstrained_reg_groups
    on ManageRegistrationGroups do |page|
      page.generate_unconstrained_reg_groups
    end
  end

  def generate_reg_groups

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
