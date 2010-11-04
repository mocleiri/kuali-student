/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sushik.client.component.infc.base.ui.HeaderLink;
import org.sushik.client.component.infc.base.ui.HeaderLinks;
import org.sushik.client.component.infc.base.ui.NestedComponent;

/**
 * A band of links across the page
 *
 * Configured as follows:
 *
 */
public class HeaderLinksImpl extends BandImpl
    implements HeaderLinks
{

 public HeaderLinksImpl ()
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
  dockPanel.setWidth ("100%");
  
  if (this.getLeftLinks () != null)
  {
   HorizontalPanel panel = new HorizontalPanel ();
   panel.setHorizontalAlignment (HorizontalPanel.ALIGN_LEFT);
   dockPanel.add (panel, DockPanel.WEST);
   panel.setSpacing (3);
   for (HeaderLink link: this.getLeftLinks ())
   {
    link.init (this);
    panel.add ((Widget) link.getImpl ());
   }
  }

  if (this.getCenterLinks () != null)
  {
   HorizontalPanel panel = new HorizontalPanel ();
   panel.setHorizontalAlignment (HorizontalPanel.ALIGN_CENTER);
   dockPanel.add (panel, DockPanel.CENTER);
   panel.setSpacing (3);
   for (HeaderLink link: this.getCenterLinks ())
   {
    link.init (this);
    panel.add ((Widget) link.getImpl ());
   }
  }

  if (this.getRightLinks () != null)
  {
   HorizontalPanel panel = new HorizontalPanel ();
   panel.setHorizontalAlignment (HorizontalPanel.ALIGN_RIGHT);
   dockPanel.add (panel, DockPanel.EAST);
   panel.setSpacing (3);
   for (HeaderLink link: this.getRightLinks ())
   {
    link.init (this);
    panel.add ((Widget) link.getImpl ());
   }
  }
 }

 @Override
 public void show ()
 {
  dockPanel.setVisible (true);
 }



	private List<HeaderLink> leftLinks;

	/**
	* Set leftLinks
	*
	* optional
	*
	* Links to appear on the left side of the page
	*/
	@Override
	public void setLeftLinks (List<HeaderLink> leftLinks)
	{
		this.leftLinks = leftLinks;
	}

	/**
	* Get leftLinks
	*
	* optional
	*
	* Links to appear on the left side of the page
	*/
	@Override
	public List<HeaderLink> getLeftLinks ()
	{
		return this.leftLinks;
	}


	private List<HeaderLink> centerLinks;

	/**
	* Set centerLinks
	*
	* optional
	*
	* Links to appear in the center of the page
	*/
	@Override
	public void setCenterLinks (List<HeaderLink> centerLinks)
	{
		this.centerLinks = centerLinks;
	}

	/**
	* Get centerLinks
	*
	* optional
	*
	* Links to appear in the center of the page
	*/
	@Override
	public List<HeaderLink> getCenterLinks ()
	{
		return this.centerLinks;
	}


	private List<HeaderLink> rightLinks;

	/**
	* Set rightLinks
	*
	* optional
	*
	* Links to appear on the right side of the page
	*/
	@Override
	public void setRightLinks (List<HeaderLink> rightLinks)
	{
		this.rightLinks = rightLinks;
	}

	/**
	* Get rightLinks
	*
	* optional
	*
	* Links to appear on the right side of the page
	*/
	@Override
	public List<HeaderLink> getRightLinks ()
	{
		return this.rightLinks;
	}

	

}

