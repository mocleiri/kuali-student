class DataFactory

  include Foundry
  include DateFactory
  include StringFactory
  include Comparable

  def linked_data_object_copy(parent_obj)
    object_copy = self.class.new(@browser, {})
    self.instance_variables.each do |var|
      key = var.to_s.gsub('@','').to_sym
      orig_val = instance_variable_get var
      case
        when orig_val.kind_of?(CollectionsFactory)
          new_collection = CollectionsFactory.new(@browser)
          orig_val.each do |item|
            new_collection << item.linked_data_object_copy(object_copy)
          end
          object_copy.instance_variable_set(var,new_collection)
        when orig_val.instance_of?(Array) || orig_val.instance_of?(Hash)
          begin
            new_list = Marshal::load(Marshal.dump(orig_val))
            object_copy.instance_variable_set(var,new_list)
          rescue TypeError
            raise %{\nKey: #{key.inspect}\nValue: #{orig_val.inspect}\nClass: #{orig_val.class}\n\nThe copying of the Data Object has thrown a TypeError,\nwhich means the object detailed above is not "Marshallable".\nThe most likely cause is that you have put\na Data Object inside an\nArray or Hash.\nIf possible, put the Data Object into a Collection.\n\n}
          end
        when orig_val.kind_of?(DataFactory)
          if key.to_s.start_with?('parent')
            object_copy.instance_variable_set(var,parent_obj)
          else
            object_copy.instance_variable_set(var,orig_val.linked_data_object_copy(object_copy))
          end
        else
          object_copy.instance_variable_set(var.to_s,orig_val)
      end
    end
    object_copy
  end


end