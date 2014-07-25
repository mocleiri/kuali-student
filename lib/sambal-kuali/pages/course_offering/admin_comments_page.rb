class AdminComments < BasePage

  expected_element :add_comment_element

  def frm
    self.iframe(class: "fancybox-iframe")
  end

  element(:loading) { |b| b.frm.image(alt: "Loading...") }

  element(:new_comment_field) { |b| b.frm.textarea(id: 'KS-NewCommentField_control') }
  element(:add_comment_element) { |b| b.frm.button(id: 'u1840ksm') } #TODO: static id
  element(:add_comment) { |b| b.add_comment_element.click; b.loading.wait_while_present }

  element(:comment_list_header) { |b| b.frm.header(id: 'Comment_list_Header') }
  value(:comment_list_header_text) { |b| b.comment_list_header.text }
  value(:header_text_comments_count){ |b| b.comment_list_header_text[/\d+(?=\))/].to_i }

  #element(:comment_list_div) { |b| b.frm.div(id: 'u1bcxmtx') }
  element(:comments_list) { |b| b.frm.divs(id: /^KS-collection-rowId_line\d+$/) }

  def comment_by_text (comment_text)
    comments_list.each do |div|
      page_comment_text = div.div(id: /^KS-CommentField_UI_ID_line\d+$/).text
      return div if page_comment_text == comment_text
    end
    nil
  end

  def comment_text(comment_div)
    comment_div.div(id: /^KS-CommentField_UI_ID_line\d+$/).text
  end

  def  comment_created_by(comment_div)
    comment_div.p(id: /^creator-name-id_line\d+$/).text
  end

  def  comment_created_date(comment_div)
    comment_div.p(id: /^creator-date-id_line\d+$/).text
  end

  def comment_text_editor(comment_div)
    comment_div.textarea(id: /^KS-CommentField_ID_line\d+_control$/)
  end

  def comment_save_edit(comment_div)
    comment_div.button(id: /^KS-CommentSaveAction_ID_line\d+$/)
  end

  def comment_cancel_edit(comment_div)
    comment_div.button(id: /^KS-CommentCancelAction_ID_line\d+$/)
  end

  def comment_edited_by(comment_div)
    comment_div.p(id: /^lastEditor-name-id_line\d+$/).text
  end

  def  comment_edited_date(comment_div)
    comment_div.p(id: /^lastEditor-date-id_line\d+$/).text
  end

  def delete_comment_element(comment_div)
    comment_div.link(id: /^KS-CommentDeleteAction_line\d+$/)
  end

  def delete_comment(comment_text, confirm_delete=true)
    comment = comment_by_text(comment_text)
    delete_comment_element(comment).click
    sleep 5
    comment = comment_by_text(comment_text) #refresh occurs
    if confirm_delete
      confirm_delete_element(comment).click
    else
      cancel_delete_element(comment).click
    end
    loading.wait_while_present
  end

  def confirm_delete_element(comment_div)
    comment_div.button(id: /^KS-CommentDeleteAction_ID_line\d+$/)
  end

  def cancel_delete_element(comment_div)
    comment_div.button(id: /^KS-CommentCancelDeleteAction_ID_line\d+$/)
  end

  def edit_comment_element(comment_div)
    comment_div.link(id: /^KS-CommentEditAction_line\d+$/)
  end


  action(:close) { |b| b.frm.button(id: 'commentsLightBoxContents-cancel').click; sleep 3 } #sleep required after closing dialog, issue across this project
end