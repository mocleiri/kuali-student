/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;
import org.sushik.client.component.infc.base.ui.CloseWindow;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.PopupTableOfDataPage;
import org.sushik.client.component.infc.base.ui.PopupTitle;
import org.sushik.client.component.infc.base.ui.TableDataSection;


/**
 * A popup window that displays a table of times from which the user may choose
 *
 * Configured as follows:
 *
 */
public class PopupTableOfDataPageImpl  extends PopupPageImpl
  implements PopupTableOfDataPage
{

 public PopupTableOfDataPageImpl ()
 {
  super ();
 }
 private DockPanel page;

 @Override
 public Object getImpl ()
 {
  return page;
 }

 @Override
 public void hide ()
 {
  page.setVisible (false);
 }

 @Override
 public void show ()
 {
  page.setVisible (true);
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  page = new DockPanel ();
  if (this.getTableDataSection () != null)
  {
   this.getTableDataSection ().init (this);
   page.add ((Widget) this.getTableDataSection ().getImpl (), DockPanel.CENTER);
  }
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


	private TableDataSection tableDataSection;

	/**
	* Set tableDataSection
	*
	* required
	*
	* The table of data
	*/
	@Override
	public void setTableDataSection (TableDataSection tableDataSection)
	{
		this.tableDataSection = tableDataSection;
	}

	/**
	* Get tableDataSection
	*
	* required
	*
	* The table of data
	*/
	@Override
	public TableDataSection getTableDataSection ()
	{
		return this.tableDataSection;
	}

	
}

