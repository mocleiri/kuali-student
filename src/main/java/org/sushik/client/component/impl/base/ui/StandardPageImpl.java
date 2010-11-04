/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.sushik.client.component.infc.base.ui.BackButton;
import org.sushik.client.component.infc.base.ui.CloseWindow;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.PageFooter;
import org.sushik.client.component.infc.base.ui.PageHeader;
import org.sushik.client.component.infc.base.ui.StandardPage;


/**
 * A standard page inside the browser window
 *
 * Configured as follows:
 *
 */
public class StandardPageImpl extends PageImpl
    implements StandardPage
{

 public StandardPageImpl ()
 {
  super ();
 }

 private DockPanel dockPanel;

 @Override
 public Object getImpl ()
 {
  return dockPanel;
 }

 @Override
 public void hide ()
 {
  dockPanel.setVisible (false);
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  dockPanel = new DockPanel ();
  dockPanel.setBorderWidth (1);
  dockPanel.setWidth ("100%");
  VerticalPanel spacer = new VerticalPanel ();
  spacer.setWidth ("3%");
  dockPanel.add (spacer, DockPanel.WEST);
  
  if (this.getPageHeader () != null)
  {
   this.getPageHeader ().setText (this.getPageTitle ());
   this.getPageHeader ().init (this);
   dockPanel.add ((Widget) this.getPageHeader ().getImpl (), DockPanel.NORTH);
  }
  if (this.getPageFooter () != null)
  {
   this.getPageFooter ().init (this);
   dockPanel.add ((Widget) this.getPageFooter ().getImpl (), DockPanel.SOUTH);
  }
 }

 @Override
 public void show ()
 {
//  RootPanel.get ().clear ();
//  RootPanel.get ().add (dockPanel);
  dockPanel.setVisible (true);
 }


	private PageHeader pageHeader;

	/**
	* Set pageHeader
	*
	* required
	*
	* Title of the popup
	*/
	@Override
	public void setPageHeader (PageHeader pageHeader)
	{
		this.pageHeader = pageHeader;
	}

	/**
	* Get pageHeader
	*
	* required
	*
	* Title of the popup
	*/
	@Override
	public PageHeader getPageHeader ()
	{
		return this.pageHeader;
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


	private BackButton backButton;

	/**
	* Set backButton
	*
	* required
	*
	* What to do if the user hits the back button
	*/
	@Override
	public void setBackButton (BackButton backButton)
	{
		this.backButton = backButton;
	}

	/**
	* Get backButton
	*
	* required
	*
	* What to do if the user hits the back button
	*/
	@Override
	public BackButton getBackButton ()
	{
		return this.backButton;
	}


	private PageFooter pageFooter;

	/**
	* Set pageFooter
	*
	* required
	*
	* Default Value:
	* Standard Page Footer
	*
	* Footer for page
	*/
	@Override
	public void setPageFooter (PageFooter pageFooter)
	{
		this.pageFooter = pageFooter;
	}

	/**
	* Get pageFooter
	*
	* required
	*
	* Footer for page
	*/
	@Override
	public PageFooter getPageFooter ()
	{
		return this.pageFooter;
	}
	
}

