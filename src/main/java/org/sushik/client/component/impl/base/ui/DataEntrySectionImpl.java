/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sushik.client.component.infc.base.ui.ContextToolbar;
import org.sushik.client.component.infc.base.ui.DataEntrySection;
import org.sushik.client.component.infc.base.ui.DataSectionLink;
import org.sushik.client.component.infc.base.ui.FieldGroup;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.TabSectionHeader;


/**
 * A section that has field groups for it's body and may have optional sections
 * breaks and tabs
 *
 * Configured as follows:
 *
 * headerStyle - Style to use for the header
 *     section.header
 *
 * currentTabStyle - The style of the current tab
 *     tab.label.current
 *
 * currentSectionStyle - Sections above the active section
 *     data.section.current
 *
 */
public class DataEntrySectionImpl extends SectionImpl
    implements DataEntrySection
{

 public DataEntrySectionImpl ()
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

  // tab panel if any
  if (this.getCurrentTabText () != null)
  {
   HorizontalPanel tabs = new HorizontalPanel ();
   panel.add (tabs);

   if (this.getLeftTabs () != null)
   {
    for (TabSectionHeader tab: this.getLeftTabs ())
    {
     tab.init (this);
     tabs.add ((Widget) tab.getImpl ());
    }
   }
   Button currentTab = new Button ();
   currentTab.setText (this.getCurrentTabText ());
   tabs.add (currentTab);
   if (this.getRightTabs () != null)
   {
    for (TabSectionHeader tab: this.getRightTabs ())
    {
     tab.init (this);
     tabs.add ((Widget) tab.getImpl ());
    }
   }
  }

  
  if (this.getToolbar () != null)
  {
   this.getToolbar ().init (this);
   this.panel.add ((Widget) this.getToolbar ().getImpl ());
  }

  // sections above
  if (this.getSectionsAbove () != null)
  {
   for (DataSectionLink sectionLink: this.getSectionsAbove ())
   {
    sectionLink.init (this);
    panel.add ((Widget) sectionLink.getImpl ());
   }
  }
  Label currentSection = new Label ();
  currentSection.setText (this.getCurrentSectionText ());
  panel.add (currentSection);

  if (this.getFieldGroups () != null)
  {
   for (FieldGroup fieldGroup: this.getFieldGroups ())
   {
    fieldGroup.init (this);
    panel.add ((Widget) fieldGroup.getImpl ());
   }
  }

  if (this.getSectionsBelow () != null)
  {
   for (DataSectionLink sectionLink: this.getSectionsBelow ())
   {
    sectionLink.init (this);
    panel.add ((Widget) sectionLink.getImpl ());
   }
  }
 }

 @Override
 public void show ()
 {
  this.panel.setVisible (true);
 }


 @Override
 public boolean isValid ()
 {
  boolean valid = true;
  for (FieldGroup fieldGroup : this.getFieldGroups ())
  {
   if ( ! fieldGroup.isValid ())
   {
    valid = false;
   }
  }
  return valid;
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


	private List<TabSectionHeader> leftTabs;

	/**
	* Set leftTabs
	*
	* optional
	*
	* Tabs that appear to the left of the current tab
	*/
	@Override
	public void setLeftTabs (List<TabSectionHeader> leftTabs)
	{
		this.leftTabs = leftTabs;
	}

	/**
	* Get leftTabs
	*
	* optional
	*
	* Tabs that appear to the left of the current tab
	*/
	@Override
	public List<TabSectionHeader> getLeftTabs ()
	{
		return this.leftTabs;
	}


	private String currentTabText;

	/**
	* Set currentTabText
	*
	* optional
	*
	* The text of the current tab
	*/
	@Override
	public void setCurrentTabText (String currentTabText)
	{
		this.currentTabText = currentTabText;
	}

	/**
	* Get currentTabText
	*
	* optional
	*
	* The text of the current tab
	*/
	@Override
	public String getCurrentTabText ()
	{
		return this.currentTabText;
	}


	private String currentTabStyle;

	/**
	* Set currentTabStyle
	*
	* optional
	*
	* Default Value:
	* tab.label.current
	*
	* The style of the current tab
	*/
	@Override
	public void setCurrentTabStyle (String currentTabStyle)
	{
		this.currentTabStyle = currentTabStyle;
	}

	/**
	* Get currentTabStyle
	*
	* optional
	*
	* The style of the current tab
	*/
	@Override
	public String getCurrentTabStyle ()
	{
		return this.currentTabStyle;
	}


	private List<TabSectionHeader> rightTabs;

	/**
	* Set rightTabs
	*
	* optional
	*
	* Tabs that appear to the right of the current tab
	*/
	@Override
	public void setRightTabs (List<TabSectionHeader> rightTabs)
	{
		this.rightTabs = rightTabs;
	}

	/**
	* Get rightTabs
	*
	* optional
	*
	* Tabs that appear to the right of the current tab
	*/
	@Override
	public List<TabSectionHeader> getRightTabs ()
	{
		return this.rightTabs;
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


	private List<DataSectionLink> sectionsAbove;

	/**
	* Set sectionsAbove
	*
	* optional
	*
	* Sections above the active section
	*/
	@Override
	public void setSectionsAbove (List<DataSectionLink> sectionsAbove)
	{
		this.sectionsAbove = sectionsAbove;
	}

	/**
	* Get sectionsAbove
	*
	* optional
	*
	* Sections above the active section
	*/
	@Override
	public List<DataSectionLink> getSectionsAbove ()
	{
		return this.sectionsAbove;
	}


	private String currentSectionText;

	/**
	* Set currentSectionText
	*
	* optional
	*
	* Sections above the active section
	*/
	@Override
	public void setCurrentSectionText (String currentSectionText)
	{
		this.currentSectionText = currentSectionText;
	}

	/**
	* Get currentSectionText
	*
	* optional
	*
	* Sections above the active section
	*/
	@Override
	public String getCurrentSectionText ()
	{
		return this.currentSectionText;
	}


	private String currentSectionStyle;

	/**
	* Set currentSectionStyle
	*
	* optional
	*
	* Default Value:
	* data.section.current
	*
	* Sections above the active section
	*/
	@Override
	public void setCurrentSectionStyle (String currentSectionStyle)
	{
		this.currentSectionStyle = currentSectionStyle;
	}

	/**
	* Get currentSectionStyle
	*
	* optional
	*
	* Sections above the active section
	*/
	@Override
	public String getCurrentSectionStyle ()
	{
		return this.currentSectionStyle;
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


	private List<DataSectionLink> sectionsBelow;

	/**
	* Set sectionsBelow
	*
	* optional
	*
	* Sections below the active section
	*/
	@Override
	public void setSectionsBelow (List<DataSectionLink> sectionsBelow)
	{
		this.sectionsBelow = sectionsBelow;
	}

	/**
	* Get sectionsBelow
	*
	* optional
	*
	* Sections below the active section
	*/
	@Override
	public List<DataSectionLink> getSectionsBelow ()
	{
		return this.sectionsBelow;
	}





}

