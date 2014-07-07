# stores test data for creating/editing and validating personnel data and provides convenience methods for navigation and data entry
#
# Personnel is a child of a ActivityOffering or CourseOffering (Affiliated Personnel)
#
# class attributes are initialized with default data unless values are explicitly provided
#
# Typical usage: (with optional setting of explicit data value in [] )
#
# personnel_list[0] = make PersonnelObject, :id=> "user1", :affiliation =>"Instructor"
#
# @activity_offering.edit :affiliated_person_list => personnel_list
#
#create generally called from ActivityOffering/CourseOffering class
# Note the use of the ruby options hash pattern re: setting attribute values
class PersonnelObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows

  #generally set using options hash
  attr_accessor :id,
                :name,
                :affiliation,
                :inst_effort,
                :context, #can be :course_offering/:activity_offering
                :parent_obj

  # provides default data:
  # defaults = {
  #    :id => "admin",
  #    :affiliation => "Instructor",
  #    :inst_effort => 50
  # }
  # initialize is generally called using TestFactory Foundry .make or .create methods
  def initialize(browser, opts={})
    @browser = browser

    defaults = {
        :id => "admin",
        :name => "admin,admin",
        :affiliation => "Instructor",
        :inst_effort => 50,
        :context => :activity_offering
    }

    set_options(defaults.merge(opts))
  end

  # creates personnel based on class attributes
  #
  # generally called from ActivityOffering class
  PERSONNEL_ID_COLUMN = 0
  PERSONNEL_NAME_COLUMN = 1
  PERSONNEL_AFFILIATION_COLUMN = 2
  PERSONNEL_INST_EFFORT_COLUMN = 3
  PERSONNEL_DELETE_COLUMN      = 4

  def create
    if @context == :activity_offering
      on ActivityOfferingMaintenance do |page|
        page.add_personnel
        page.loading.wait_while_present
        page.add_personnel_id.set @id
        page.add_personnel_name.set @name
        page.add_personnel_affiliation.select(@affiliation)
        page.loading.wait_while_present
        page.add_personnel_inst_effort.set @inst_effort
      end
    else
      on CourseOfferingCreateEdit do |page|
        page.lookup_person
      end
      on PersonnelLookup do |page|
        page.principal_name.set @id
        page.search
        page.return_value(@id)
      end
      on CourseOfferingCreateEdit do |page|
        page.add_affiliation.select(@affiliation)
        page.add_personnel
      end
    end
  end

  # edits personnel based on values in options hash
  #
  #  @param opts [Hash] key => value for attribute to be updated
  def edit opts={}
    defaults = {
        :config_only => false,
        :defer_save => false,
        :start_edit => true
    }
    options = defaults.merge(opts)

    @parent_obj.edit :defer_save => true if options[:start_edit]

    if @context == :activity_offering
      #TODO: only edits the first row
      on ActivityOfferingMaintenance do |page|
        page.personnel_table.rows[1].cells[PERSONNEL_ID_COLUMN].text_field.set options[:id] unless options[:id].nil?
        page.personnel_table.rows[1].cells[PERSONNEL_NAME_COLUMN].text_field.set options[:name] unless options[:name].nil?
        page.personnel_table.rows[1].cells[PERSONNEL_AFFILIATION_COLUMN].select().select(options[:affiliation]) unless options[:affiliation].nil?
        page.personnel_table.rows[1].cells[PERSONNEL_INST_EFFORT_COLUMN].text_field.set options[:inst_effort] unless options[:inst_effort].nil?
      end
    else
      #TODO
    end
    @parent_obj.save unless options[:defer_save]
    update_options(options)

  end

  def target_row_by_personnel_id
    on ActivityOfferingMaintenance do |page|
      idx = -1
      page.personnel_table.rows.each do |row|
        idx += 1
        if (idx < 1)
          next
        end
        begin
          row_key = row.cells[PERSONNEL_ID_COLUMN].text_field.value
          return row unless row_key != @id
        rescue Watir::Exception::UnknownObjectException
          return nil
        end
      end
    end
    return nil
  end

end

class PersonnelCollection < CollectionsFactory

  contains PersonnelObject

end

