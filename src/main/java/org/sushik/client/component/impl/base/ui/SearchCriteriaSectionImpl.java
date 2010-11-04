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
import org.sushik.client.component.infc.base.ui.FieldGroup;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.SearchCriteriaSection;


/**
* A section that captures search criteria
*
* Configured as follows:
*
* headerStyle - Style to use for the header
*     section.header
*
*/
public class SearchCriteriaSectionImpl extends SectionImpl
 implements SearchCriteriaSection
{
	
	public SearchCriteriaSectionImpl ()
	{
		super ();
	}

 private VerticalPanel panel;

 @Override
 public Object getImpl ()
 {
  return this.panel;
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
  panel = new VerticalPanel ();
  if (this.getHeaderText () != null)
  {
   Label label = new Label ();
   label.setText (this.getHeaderText ());
   panel.add (label);
  }
  if (this.getFieldGroups () != null)
  {
   for (FieldGroup fg : this.getFieldGroups ())
   {
    fg.init (this);
    this.panel.add ((Widget) fg.getImpl ());
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
	* The header to display at the top of this section
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
	* The header to display at the top of this section
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


	private List<FieldGroup> fieldGroups;

	/**
	* Set fieldGroups
	*
	* optional
	*
	* Group of search criteria fields to be prompted for for searching
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
	* Group of search criteria fields to be prompted for for searching
	*/
	@Override
	public List<FieldGroup> getFieldGroups ()
	{
		return this.fieldGroups;
	}
	
}

