/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;
import org.sushik.client.component.infc.base.ui.CloseWindow;
import org.sushik.client.component.infc.base.ui.ContextToolbar;
import org.sushik.client.component.infc.base.ui.DataEntrySection;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.PopupDataEntryWindow;
import org.sushik.client.component.infc.base.ui.PopupTitle;


/**
 * A popup window that accepts typically a small amount of data input from the user
 *
 * Configured as follows:
 *
 */
public class PopupDataEntryWindowImpl extends PopupPageImpl
    implements PopupDataEntryWindow
{

 public PopupDataEntryWindowImpl ()
 {
  super ();
 }

 private DockPanel panel;

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
  panel = new DockPanel ();

  if (this.getToolbar () != null)
  {
   this.getToolbar ().init (this);
   panel.add ((Widget) this.getToolbar ().getImpl (), DockPanel.NORTH);
  }

  if (this.getDataEntrySection () != null)
  {
   this.getDataEntrySection ().init (this);
   panel.add ((Widget) this.getDataEntrySection ().getImpl (), DockPanel.CENTER);
  }
 }

 @Override
 public void show ()
 {
  this.panel.setVisible (true);
 }


	private CloseWindow closeWindow;

	/**
	* Set closeWindow
	*
	* required
	*
	* What to do if the user closes the window
	*/
	@Override
	public void setCloseWindow (CloseWindow closeWindow)
	{
		this.closeWindow = closeWindow;
	}

	/**
	* Get closeWindow
	*
	* required
	*
	* What to do if the user closes the window
	*/
	@Override
	public CloseWindow getCloseWindow ()
	{
		return this.closeWindow;
	}


	private PopupTitle popupTitle;

	/**
	* Set popupTitle
	*
	* required
	*
	* Title of the popup
	*/
	@Override
	public void setPopupTitle (PopupTitle popupTitle)
	{
		this.popupTitle = popupTitle;
	}

	/**
	* Get popupTitle
	*
	* required
	*
	* Title of the popup
	*/
	@Override
	public PopupTitle getPopupTitle ()
	{
		return this.popupTitle;
	}


	private ContextToolbar toolbar;

	/**
	* Set toolbar
	*
	* optional
	*
	* Bar of buttons for user to navigate
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
	* Bar of buttons for user to navigate
	*/
	@Override
	public ContextToolbar getToolbar ()
	{
		return this.toolbar;
	}


	private DataEntrySection dataEntrySection;

	/**
	* Set dataEntrySection
	*
	* required
	*
	* The section that holds the data fields
	*/
	@Override
	public void setDataEntrySection (DataEntrySection dataEntrySection)
	{
		this.dataEntrySection = dataEntrySection;
	}

	/**
	* Get dataEntrySection
	*
	* required
	*
	* The section that holds the data fields
	*/
	@Override
	public DataEntrySection getDataEntrySection ()
	{
		return this.dataEntrySection;
	}



}

