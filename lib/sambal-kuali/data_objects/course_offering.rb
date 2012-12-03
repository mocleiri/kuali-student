class CourseOffering

  include Foundry
  include DataFactory
  include DateFactory
  include StringFactory
  include Workflows

  attr_accessor :term,
                :course,
                :suffix,
                :activity_offering_cluster_list,
                :ao_list


  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :term=>"20122",
        :course=>"ENGL103",
        :suffix=>"",
        :activity_offering_cluster_list=>[],
        :ao_list => []
    }
    options = defaults.merge(opts)
    set_options(options)
  end

  def manage
    go_to_manage_course_offerings
    on ManageCourseOfferings do |page|
      page.term.set @term
      page.input_code.set @course
      page.show
    end
    on ManageCourseOfferings do |page|
      @ao_list = page.codes_list
    end
  end

  def manage_registration_groups
    on ManageCourseOfferings do |page|
      page.manage_registration_groups
    end
    #init

  end

  def add_ao_cluster(ao_cluster)
    @activity_offering_cluster_list << ao_cluster
  end

  def add_aos_to_clusters
    @activity_offering_cluster_list.each do |cluster|
      cluster.add_unassigned_aos
    end
  end

  def expected_unassigned_ao_list
    expected_unassigned = @ao_list
    @activity_offering_cluster_list.each do |cluster|
      expected_unassigned = expected_unassigned - cluster.assigned_ao_list
    end
    expected_unassigned
  end

end
