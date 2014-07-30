class AdminCommentObject < DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Workflows

  attr_accessor :text,
                :created_date,
                :creator,
                :last_editor,
                :edited_date,
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
      new_comment = page.comment_by_text(@text)
      created_by = page.comment_created_by(new_comment)
      created_date = page.comment_created_date(new_comment)
    end

    @creator = created_by
    @created_date = created_date
  end

  def edit opts={}
    defaults = {
        :do_navigation => true,
        :cancel_edit => false,
        :close_comments_dialog => true
    }
    options = defaults.merge(opts)

    if options[:do_navigation]
      @parent_obj.manage_comments
    end

    edit_comment = nil

    if options[:text] != nil
      on AdminComments do |page|
        edit_comment = page.comment_by_text(@text)
        page.edit_comment_element(edit_comment).click
        page.comment_text_editor(edit_comment).clear
        page.comment_text_editor(edit_comment).set options[:text]
      end
    end

    if ! options[:cancel_edit]
      edited_by = ''
      edited_date = ''
      on AdminComments do |page|
        page.comment_save_edit(edit_comment).click
        page.loading.wait_while_present
        sleep 1
        @text = options[:text]
      end
      on AdminComments do |page|
        edit_comment = page.comment_by_text(@text)
        @last_editor = page.comment_edited_by(edit_comment)
        @edited_date = page.comment_edited_date(edit_comment)
      end
    else
      on(AdminComments).comment_cancel_edit(edit_comment)
    end
    on(AdminComments).close if options[:close_comments_dialog]
  end

  def delete opts={}
    defaults = {
        :do_navigation => false,
        :confirm_delete => true,
        :close_comments_dialog => true
    }
    options = defaults.merge(opts)

    if options[:do_navigation]
      @parent_obj.manage_comments
    end

    on AdminComments do |page| #synch to page again
      page.delete_comment(@text, options[:confirm_delete])
      page.close if options[:close_comments_dialog]
    end
    @parent_obj.admin_comments_list.delete(self) if options[:confirm_delete]
  end

end

class AdminCommentCollection < CollectionsFactory
  contains AdminCommentObject

end