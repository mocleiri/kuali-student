#!/usr/bin/env ruby

# 
# == Synopsis
#
# Creates a locator object that contains all locator information for selenium testing.
#
# Author:: Kyle Campos (mailto:kcampos@rsmart.com)
#

class Locators
  
  attr_accessor :locator
  
  def initialize(locator)
    @locator = locator
  end
  
end


class RiceLocators < Locators
  
  attr_accessor :type, :value, :locator
    
  def initialize(locator='', type='name', value='')
    
    @type = type
    @value = value
    super(locator)
    
  end
  
  def locator(locator="#{@type}=#{@value}")
    @locator=locator
  end
  
  def login_username
    self.type = 'name'
    self.value = '__login_user'
    self.locator
  end
  
  def create_travel_request
    self.type  = 'link'
    self.value = 'Create New Sample Application Travel Request (KualiDocumentActionBase)'
    self.locator
  end
  
  def travel_request_description
    self.type  = 'name'
    self.value = 'document.documentHeader.documentDescription'
    self.locator
  end
  
  def travel_request_name
    self.type  = 'name'
    self.value = 'document.traveler'
    self.locator
  end
  
  def travel_request_origin
    self.type  = 'name'
    self.value = 'document.origin'
    self.locator
  end
  
  def travel_request_destination
    self.type  = 'name'
    self.value = 'document.destination'
    self.locator
  end
  
  def travel_request_type
    self.type  = 'name'
    self.value = 'document.requestType'
    self.locator
  end
  
  def travel_request_account
    self.type  = 'name'
    self.value = 'travelAccount.number'
    self.locator
  end
  
  def travel_request_account_add
    self.type  = 'name'
    self.value = 'methodToCall.insertAccount'
    self.locator
  end
  
  def travel_request_submit
    self.type  = 'name'
    self.value = 'methodToCall.route'
    self.locator
  end
  
  def travel_request_notes_show
    self.type  = 'name'
    self.value = 'methodToCall.toggleTab.tabNotesandAttachments'
    self.locator
  end
  
  def travel_request_notes_text
    self.type  = 'name'
    self.value = 'newNote.noteText'
    self.locator
  end
  
  def travel_request_notes_add
    self.type  = 'name'
    self.value = 'methodToCall.insertBONote'
    self.locator
  end
  
end