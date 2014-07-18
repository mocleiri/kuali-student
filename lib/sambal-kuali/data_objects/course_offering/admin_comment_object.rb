class AdminCommentObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows

  attr_accessor :created_date,
                :creator,
                :parent_obj

  def initialize(browser, opts={})
    @browser = browser
    #no defaults
    set_options(options)
  end

  def create
    # manage parent CO
    #
  end

  #need to specify both :org_id & :org_name
  def edit opts={}
    defaults = {
        :defer_save => false,
        :start_edit => true
    }
    options = defaults.merge(opts)

    @parent_co.edit :defer_save => true if options[:start_edit]

    if !options[:org_id].nil? && !options[:org_name].nil?
      on CourseOfferingCreateEdit do |page|
        row = page.target_orgs_row(@org_id)
        row.cells[ORG_ID_COLUMN].button().click
        page.loading.wait_while_present
      end

      on OrgLookupPopUp do |page|
        page.long_name.set options[:org_name]
        page.search
        page.return_value options[:org_id]
      end
    end

    @parent_co.save unless options[:defer_save]
    set_options(options)
  end



end

class AdminCommentCollection < CollectionsFactory
  contains AdminCommentObject
end