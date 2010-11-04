/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sushik.client.component.infc.base.ui.Field;
import org.sushik.client.component.infc.base.ui.FieldGroup;
import org.sushik.client.component.infc.base.ui.MoreDetailLink;
import org.sushik.client.component.infc.base.ui.NestedComponent;


/**
 * An abstract group of fields either for display or data entry or gathering
 * criteria for searching
 *
 * Configured as follows:
 *
 * style - style for the label associated with this field group
 *     field.group
 *
 */
public class FieldGroupImpl extends ContainerImpl
  implements FieldGroup
{

 public FieldGroupImpl ()
 {
  super ();
 }
 private FlexTable table;

 @Override
 public Object getImpl ()
 {
  return table;
 }
 
 @Override
 public void hide ()
 {
  this.table.setVisible (false);
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  this.table = new FlexTable ();

  if (this.getText () != null)
  {
   Label label = new Label ();
   label.setText (this.getText ());
   table.setWidget (1, 0, label);
  }

  if (this.getFields () != null)
  {
   int col = 0;
   for (Field field : this.getFields ())
   {
    field.init (this);
    col ++;
    if (field.getFieldLabelText () != null)
    {
     Label label = new Label ();
     label.setText (field.getFieldLabelText ());
     table.setWidget (0, col, label);
    }
    field.init (this);
    Widget fieldImpl = (Widget) field.getImpl ();
    table.setWidget (1, col, fieldImpl);
   }
  }
 }

 @Override
 public void show ()
 {
  this.table.setVisible (true);
 }

 @Override
 public boolean isValid ()
 {
  for (Field field : this.getFields ())
  {
   if ( ! field.isValid ())
   {
    return false;
   }
  }
  return true;
 }

 	private String text;

	/**
	* Set text
	*
	* required
	*
	* An optional label to display as the header for this group of fields
	*/
	@Override
	public void setText (String text)
	{
		this.text = text;
	}

	/**
	* Get text
	*
	* required
	*
	* An optional label to display as the header for this group of fields
	*/
	@Override
	public String getText ()
	{
		return this.text;
	}


	private String style;

	/**
	* Set style
	*
	* required
	*
	* Default Value:
	* field-group
	*
	* style for the label associated with this field group
	*/
	@Override
	public void setStyle (String style)
	{
		this.style = style;
	}

	/**
	* Get style
	*
	* required
	*
	* style for the label associated with this field group
	*/
	@Override
	public String getStyle ()
	{
		return this.style;
	}


	private List<String> dictionaryKeys;

	/**
	* Set dictionaryKeys
	*
	* optional
	*
	* Key used to identifiy a message structure field in the dictionary
	*/
	@Override
	public void setDictionaryKeys (List<String> dictionaryKeys)
	{
		this.dictionaryKeys = dictionaryKeys;
	}

	/**
	* Get dictionaryKeys
	*
	* optional
	*
	* Key used to identifiy a message structure field in the dictionary
	*/
	@Override
	public List<String> getDictionaryKeys ()
	{
		return this.dictionaryKeys;
	}


	private List<Field> fields;

	/**
	* Set fields
	*
	* optional
	*
	* The fields in this group
	*/
	@Override
	public void setFields (List<Field> fields)
	{
		this.fields = fields;
	}

	/**
	* Get fields
	*
	* optional
	*
	* The fields in this group
	*/
	@Override
	public List<Field> getFields ()
	{
		return this.fields;
	}


	private String errorText;

	/**
	* Set errorText
	*
	* required
	*
	* An error message
	*/
	@Override
	public void setErrorText (String errorText)
	{
		this.errorText = errorText;
	}

	/**
	* Get errorText
	*
	* required
	*
	* An error message
	*/
	@Override
	public String getErrorText ()
	{
		return this.errorText;
	}


	private String errorStyle;

	/**
	* Set errorStyle
	*
	* required
	*
	* Default Value:
	* field-group-error
	*
	* style for the label associated with this error
	*/
	@Override
	public void setErrorStyle (String errorStyle)
	{
		this.errorStyle = errorStyle;
	}

	/**
	* Get errorStyle
	*
	* required
	*
	* style for the label associated with this error
	*/
	@Override
	public String getErrorStyle ()
	{
		return this.errorStyle;
	}


	private MoreDetailLink moreDetailLink;

	/**
	* Set moreDetailLink
	*
	* optional
	*
	* A link intended to show more detail
	*/
	@Override
	public void setMoreDetailLink (MoreDetailLink moreDetailLink)
	{
		this.moreDetailLink = moreDetailLink;
	}

	/**
	* Get moreDetailLink
	*
	* optional
	*
	* A link intended to show more detail
	*/
	@Override
	public MoreDetailLink getMoreDetailLink ()
	{
		return this.moreDetailLink;
	}


}

