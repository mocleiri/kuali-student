class AdminCommentObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows

  attr_accessor :text,
                :created_date,
                :creator,
                :parent_obj

  def initialize(browser, opts={})
    @browser = browser
    defaults = {
        :text => "comment text #{random_alphanums(4)}"
    }
    options = defaults.merge(opts)
    set_options(options)
  end

  def create
    created_by = ''
    created_date = ''
    on AdminComments do |page|
      page.new_comment_field.set @text
      page.add_comment
    end
    on AdminComments do |page| #synch to page again
      new_comment_div = page.comment_div_by_text(@text)
      created_by = page.comment_created_by(new_comment_div)
      created_date = page.comment_created_date(new_comment_div)
    end

    @creator = created_by
    @created_date = created_date
  end

  def manage_comments
    @parent_obj.parent_co.manage
    on(ManageCourseOfferings) do |page|

    end
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