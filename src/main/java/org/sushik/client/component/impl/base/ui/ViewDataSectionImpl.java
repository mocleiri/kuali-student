/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sushik.client.component.infc.base.ui.ContextToolbar;
import org.sushik.client.component.infc.base.ui.FieldGroup;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.ViewDataSection;



/**
* A section that has field groups for it's body that appears as a tabbed page
*
* Configured as follows:
*
* headerStyle - Style to use for the header
*     section.header
*
*/
public class ViewDataSectionImpl  extends SectionImpl
 implements ViewDataSection
{
	
	public ViewDataSectionImpl ()
	{
		super ();
	}

 private VerticalPanel panel;

 @Override
 public Object getImpl ()
 {
  return panel;
 }

 @Override
 public void hide ()
 {
  this.panel.setVisible (false);
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  this.panel = new VerticalPanel ();
  if (this.getHeaderText () != null)
  {
   Label label = new Label ();
   label.setText (this.getHeaderText ());
   panel.add (label);
  }

  if (this.getToolbar () != null)
  {
   this.getToolbar ().init (this);
   this.panel.add ((Widget) this.getToolbar ().getImpl ());
  }

 
  if (this.getFieldGroups () != null)
  {
   for (FieldGroup fieldGroup: this.getFieldGroups ())
   {
    fieldGroup.init (this);
    panel.add ((Widget) fieldGroup.getImpl ());
   }
  }
 }

 @Override
 public void show ()
 {
  this.panel.setVisible (true);
 }


	private String headerText;

	/**
	* Set headerText
	*
	* optional
	*
	* Header to be displayed at the top of this section
	*/
	@Override
	public void setHeaderText (String headerText)
	{
		this.headerText = headerText;
	}

	/**
	* Get headerText
	*
	* optional
	*
	* Header to be displayed at the top of this section
	*/
	@Override
	public String getHeaderText ()
	{
		return this.headerText;
	}


	private String headerStyle;

	/**
	* Set headerStyle
	*
	* optional
	*
	* Default Value:
	* section-header
	*
	* Style to use for the header
	*/
	@Override
	public void setHeaderStyle (String headerStyle)
	{
		this.headerStyle = headerStyle;
	}

	/**
	* Get headerStyle
	*
	* optional
	*
	* Style to use for the header
	*/
	@Override
	public String getHeaderStyle ()
	{
		return this.headerStyle;
	}


	private ContextToolbar toolbar;

	/**
	* Set toolbar
	*
	* optional
	*
	* The toolbar to display in this context
	*/
	@Override
	public void setToolbar (ContextToolbar toolbar)
	{
		this.toolbar = toolbar;
	}

	/**
	* Get toolbar
	*
	* optional
	*
	* The toolbar to display in this context
	*/
	@Override
	public ContextToolbar getToolbar ()
	{
		return this.toolbar;
	}


	private List<FieldGroup> fieldGroups;

	/**
	* Set fieldGroups
	*
	* optional
	*
	* The groups of fields to be displayed for data entry
	*/
	@Override
	public void setFieldGroups (List<FieldGroup> fieldGroups)
	{
		this.fieldGroups = fieldGroups;
	}

	/**
	* Get fieldGroups
	*
	* optional
	*
	* The groups of fields to be displayed for data entry
	*/
	@Override
	public List<FieldGroup> getFieldGroups ()
	{
		return this.fieldGroups;
	}


}

