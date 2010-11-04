/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import java.util.Date;
import org.sushik.client.component.infc.base.ui.Field;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.dictionary.api.FieldDescriptorInfo;
import org.sushik.client.manual.validation.DateHelper;

/**
 * A structure that binds a field to a widget and label and metadata
 *
 * Configured as follows:
 *
 * fieldValueStyle - The actual widget used to display the field's value
 *     field.value
 *
 * fieldLabelStyle - Links displayed as part of the header
 *     field.label
 *
 * helpStyle - The style of the text
 *     field.help
 *
 */
public class FieldImpl extends UserInteractableUIComponentImpl
  implements Field
{

 public FieldImpl ()
 {
  super ();
 }
 private Widget impl;

 @Override
 public Object getImpl ()
 {
  return impl;
 }

 @Override
 public void hide ()
 {
  impl.setVisible (false);
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  impl = this.calculateImpl ();
  if (this.getHelpText () != null)
  {
   impl.setTitle (this.getFieldLabelText ());
  }
  this.setEnabled (false);
 }

 	private String fieldKey;

	/**
	* Set fieldKey
	*
	* required
	*
	* Identifier for this field
	*/
	@Override
	public void setFieldKey (String fieldKey)
	{
		this.fieldKey = fieldKey;
	}

	/**
	* Get fieldKey
	*
	* required
	*
	* Identifier for this field
	*/
	@Override
	public String getFieldKey ()
	{
		return this.fieldKey;
	}


	private String defaultValue;

	/**
	* Set defaultValue
	*
	* required
	*
	* The default value for this field
	*/
	@Override
	public void setDefaultValue (String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	/**
	* Get defaultValue
	*
	* required
	*
	* The default value for this field
	*/
	@Override
	public String getDefaultValue ()
	{
		return this.defaultValue;
	}


	private String fieldValueStyle;

	/**
	* Set fieldValueStyle
	*
	* required
	*
	* Default Value:
	* field.value
	*
	* The actual widget used to display the field's value
	*/
	@Override
	public void setFieldValueStyle (String fieldValueStyle)
	{
		this.fieldValueStyle = fieldValueStyle;
	}

	/**
	* Get fieldValueStyle
	*
	* required
	*
	* The actual widget used to display the field's value
	*/
	@Override
	public String getFieldValueStyle ()
	{
		return this.fieldValueStyle;
	}


	private String fieldLabelText;

	/**
	* Set fieldLabelText
	*
	* optional
	*
	* Text value displayed
	*/
	@Override
	public void setFieldLabelText (String fieldLabelText)
	{
		this.fieldLabelText = fieldLabelText;
	}

	/**
	* Get fieldLabelText
	*
	* optional
	*
	* Text value displayed
	*/
	@Override
	public String getFieldLabelText ()
	{
		return this.fieldLabelText;
	}


	private String fieldLabelStyle;

	/**
	* Set fieldLabelStyle
	*
	* optional
	*
	* Default Value:
	* field.label
	*
	* Links displayed as part of the header
	*/
	@Override
	public void setFieldLabelStyle (String fieldLabelStyle)
	{
		this.fieldLabelStyle = fieldLabelStyle;
	}

	/**
	* Get fieldLabelStyle
	*
	* optional
	*
	* Links displayed as part of the header
	*/
	@Override
	public String getFieldLabelStyle ()
	{
		return this.fieldLabelStyle;
	}


	private String helpText;

	/**
	* Set helpText
	*
	* optional
	*
	* Text displayed to help guide the user in entering data
	*/
	@Override
	public void setHelpText (String helpText)
	{
		this.helpText = helpText;
	}

	/**
	* Get helpText
	*
	* optional
	*
	* Text displayed to help guide the user in entering data
	*/
	@Override
	public String getHelpText ()
	{
		return this.helpText;
	}


	private String helpStyle;

	/**
	* Set helpStyle
	*
	* optional
	*
	* Default Value:
	* field-help
	*
	* The style of the help text
	*/
	@Override
	public void setHelpStyle (String helpStyle)
	{
		this.helpStyle = helpStyle;
	}

	/**
	* Get helpStyle
	*
	* optional
	*
	* The style of the help text
	*/
	@Override
	public String getHelpStyle ()
	{
		return this.helpStyle;
	}


	private String dataType;

	/**
	* Set dataType
	*
	* required
	*
	* Indicates the type of data held by this field
	*/
	@Override
	public void setDataType (String dataType)
	{
		this.dataType = dataType;
	}

	/**
	* Get dataType
	*
	* required
	*
	* Indicates the type of data held by this field
	*/
	@Override
	public String getDataType ()
	{
		return this.dataType;
	}


	private String widgetType;

	/**
	* Set widgetType
	*
	* required
	*
	* Indicates the type fo field that is/should be used
	*/
	@Override
	public void setWidgetType (String widgetType)
	{
		this.widgetType = widgetType;
	}

	/**
	* Get widgetType
	*
	* required
	*
	* Indicates the type fo field that is/should be used
	*/
	@Override
	public String getWidgetType ()
	{
		return this.widgetType;
	}


	private Boolean readOnly;

	/**
	* Set readOnly
	*
	* required
	*
	* Indicates whether the field can be updated
	*/
	@Override
	public void setReadOnly (Boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	/**
	* Get readOnly
	*
	* required
	*
	* Indicates whether the field can be updated
	*/
	@Override
	public Boolean isReadOnly ()
	{
		return this.readOnly;
	}


	private Boolean required;

	/**
	* Set required
	*
	* required
	*
	* Indicates whether the field is required
	*/
	@Override
	public void setRequired (Boolean required)
	{
		this.required = required;
	}

	/**
	* Get required
	*
	* required
	*
	* Indicates whether the field is required
	*/
	@Override
	public Boolean isRequired ()
	{
		return this.required;
	}


	private Integer maxSize;

	/**
	* Set maxSize
	*
	* optional
	*
	* Maximum size for data entry
	*/
	@Override
	public void setMaxSize (Integer maxSize)
	{
		this.maxSize = maxSize;
	}

	/**
	* Get maxSize
	*
	* optional
	*
	* Maximum size for data entry
	*/
	@Override
	public Integer getMaxSize ()
	{
		return this.maxSize;
	}


	private Integer expectedSize;

	/**
	* Set expectedSize
	*
	* optional
	*
	* Expected or normal size
	*/
	@Override
	public void setExpectedSize (Integer expectedSize)
	{
		this.expectedSize = expectedSize;
	}

	/**
	* Get expectedSize
	*
	* optional
	*
	* Expected or normal size
	*/
	@Override
	public Integer getExpectedSize ()
	{
		return this.expectedSize;
	}



 /**
  * Calculates and configures the best ui object to use as the implementation of this field.
  * 
  * For example a simple TextBox for a small text field, DatePicker for a date field, etc
  * 
  * Intended to be overriden by a sub-class to provide more fine grained control over the implemenation
  * 
  * @return the UIObject 
  */
 private Widget calculateImpl ()
 {
  if (this.getWidgetType () == null)
  {
   this.setWidgetType (this.calculateWidgetType ());
  }

  if (this.getWidgetType ().equalsIgnoreCase (TEXT_BOX))
  {
   TextBox textBox = new TextBox ();
   if (isReadOnly () != null)
   {
    textBox.setReadOnly (isReadOnly ());
   }
   if (getMaxSize () != null)
   {
    textBox.setMaxLength (getMaxSize ().intValue ());
   }
   if (getExpectedSize () != null)
   {
    textBox.setVisibleLength (getExpectedSize ().intValue ());
   }
   return textBox;
  }

  if (this.getWidgetType ().equalsIgnoreCase (CHECK_BOX))
  {
   CheckBox checkBox = new CheckBox ();
   if (isReadOnly () != null)
   {
    // TODO: handle readonly checkboxes
//    checkBox.setReadOnly (isReadOnly ());
   }
   return checkBox;
  }

  if (this.getWidgetType ().equalsIgnoreCase (DATE_PICKER))
  {
   DatePicker datePicker = new DatePicker ();
   if (isReadOnly () != null)
   {
    // TODO: handle readonly datepickers
//    datePicker.setReadOnly (isReadOnly ());
   }
   return datePicker;
  }

  if (this.getWidgetType ().equalsIgnoreCase (PASSWORD_TEXT_BOX))
  {
   PasswordTextBox pwTextBox = new PasswordTextBox ();
   if (this.isReadOnly () != null)
   {
    pwTextBox.setReadOnly (isReadOnly ());
   }
   if (getMaxSize () != null)
   {
    pwTextBox.setMaxLength (getMaxSize ().intValue ());
   }
   if (getExpectedSize () != null)
   {
    pwTextBox.setVisibleLength (getExpectedSize ().intValue ());
   }
   return pwTextBox;
  }

  throw new IllegalArgumentException ("trying to calculate the implementation of field "
                                      + getFieldKey ()
                                      + " but encountered an unknown/unhandled widget type "
                                      + getWidgetType ());
 }
 public static final String LABEL = "Label";
 public static final String TEXT_BOX = "TextBox";
 public static final String PASSWORD_TEXT_BOX = "PasswordTextBox";
 public static final String TEXT_AREA = "TextArea";
 public static final String CHECK_BOX = "CheckBox";
 public static final String DATE_PICKER = "DatePicker";
 public static final String DATE = "Date";
 public static final String BOOLEAN = "Boolean";
 public static final String NUMBER = "Number";
 public static final String INTEGER = "Integer";
 public static final String TEXT = "Text";

 /**
  * Calculates the best widgetType to use as the implementation of this field.
  *
  * Invoked if the widget type is not specified.
  *
  * For example a simple TextBox for a small text field, DatePicker for a date field, etc
  *
  * @return the widgetType
  */
 public String calculateWidgetType ()
 {
  if (this.getDataType () != null)
  {
   if (this.getDataType ().equalsIgnoreCase (DATE))
   {
    return DATE_PICKER;
   }
   if (this.getDataType ().equalsIgnoreCase (BOOLEAN))
   {
    return CHECK_BOX;
   }
   if (this.getMaxSize () != null)
   {
    if (this.getMaxSize () > 100)
    {
     return TEXT_AREA;
    }
   }
  }
  return TEXT_BOX;

 }

 @Override
 public void show ()
 {
  impl.setVisible (true);
 }

 @Override
 public Object getValue ()
 {
  if (impl instanceof HasValue)
  {
   HasValue hasValue = (HasValue) impl;
   return hasValue.getValue ();
  }
  if (impl instanceof HasText)
  {
   HasText hasText = (HasText) impl;
   return hasText.getText ();
  }
  throw new IllegalArgumentException ("Trying to get the value of field "
                                      + getFieldKey ()
                                      + " but encountered an unknown/unhandled implementation type "
                                      + impl.getClass ().getName ());
 }

 @Override
 public void setValue (Object value)
 {
  if (impl instanceof HasValue)
  {
   HasValue hasValue = (HasValue) impl;
   hasValue.setValue (value);
   return;
  }
  if (impl instanceof HasText)
  {
   HasText hasText = (HasText) impl;
   hasText.setText ((String) value);
   return;
  }
  throw new IllegalArgumentException ("trying to set the value of field "
                                      + getFieldKey ()
                                      + " but encountered an unknown/unhandled implementation type "
                                      + impl.getClass ().getName ());
 }

 @Override
 public String getValueString ()
 {
  Object value = this.getValue ();
  if (value == null)
  {
   return null;
  }
  if (value instanceof Date)
  {
   return new DateHelper ().asYYYYMMDD ((Date) value);
  }
  return value.toString ();
 }

 @Override
 public void setValueString (String value)
 {
  if (this.getDataType () == null)
  {
   setValue (value);
   return;
  }
  if (this.getDataType ().equalsIgnoreCase (TEXT))
  {
   setValue (value);
   return;
  }
  if (this.getDataType ().equalsIgnoreCase (DATE))
  {
   setValue (new DateHelper ().asDate (value));
   return;
  }
  if (this.getDataType ().equalsIgnoreCase (BOOLEAN))
  {
   setValue (Boolean.parseBoolean (value));
  }
  if (this.getDataType ().equalsIgnoreCase (INTEGER))
  {
   setValue (Integer.parseInt (value));
  }
  throw new IllegalArgumentException ("trying to set the value of field "
                                      + getFieldKey ()
                                      + " but encountered an unknown/unhandled data type "
                                      + getDataType ());
 }

 @Override
 public boolean isValid ()
 {
  if (isRequired () != null)
  {
   if (isRequired ())
   {
    if (this.getValue () == null)
    {
     return false;
    }
   }
  }

  if (this.getMaxSize () != null)
  {
   if (this.getValueString ().length () > this.getMaxSize ())
   {
    return false;
   }
  }
  return true;
 }

 @Override
 public boolean isEnabled ()
 {
  if (impl instanceof FocusWidget)
  {
   FocusWidget focusWidget = (FocusWidget) impl;
   return focusWidget.isEnabled ();
  }
  throw new UnsupportedOperationException (
    impl.getClass ().getName ()
    + " is not a type that supports being set to enabled.");
 }

 @Override
 public void setEnabled (boolean enabled)
 {
  if (impl instanceof FocusWidget)
  {
   FocusWidget focusWidget = (FocusWidget) impl;
   focusWidget.setEnabled (enabled);
   return;
  }
  throw new UnsupportedOperationException (
    impl.getClass ().getName ()
    + " is not a type that supports being set to enabled.");

 }
}

