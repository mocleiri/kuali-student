class CmCommentsObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows
  include Utilities

  attr_accessor  :commentText, :index




  def initialize(browser, opts={})
    @browser = browser
    defaults = {
        index: 0,
        commentText: random_alphanums(10,'test proposal comment '),
    }
    set_options(defaults.merge(opts))
  end

  def create
    on CmProposalComments do |page|
      page.comment_text_input.set @commentText
      page.add_comment
      page.loading_wait
    end
  end

  def edit (opts={})
    on CmProposalComments do |page|
      page.edit_comment(opts[:index])
      if(page.alert.exists?)
        page.alert.ok
      end
      page.edit_comment_text_field(opts[:index]).set opts[:commentText]
      page.save_edited_comment(opts[:index])
      sleep 1
    end
    set_options(opts)
  end



  def delete (opts={})
    on CmProposalComments do |page|
      page.delete_comment(opts[:index])
      if(page.alert.exists?)
        page.alert.ok
      end
    end
  end

  def close_comment_dialog()
    on CmProposalComments do |page|
      page.close_dialog
      page.loading_wait
    end
    sleep 1
  end
end