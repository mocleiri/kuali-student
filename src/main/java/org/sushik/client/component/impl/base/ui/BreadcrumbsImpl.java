/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sushik.client.component.infc.base.ui.BreadcrumbLink;
import org.sushik.client.component.infc.base.ui.Breadcrumbs;
import org.sushik.client.component.infc.base.ui.NestedComponent;


/**
 * A Trail of breadcrumbs for back tracking
 *
 * Configured as follows:
 *
 */
public class BreadcrumbsImpl extends BandImpl
    implements Breadcrumbs
{

 public BreadcrumbsImpl ()
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
  panel.setVisible (false);
 }


 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  panel = new DockPanel ();
  HorizontalPanel linksPanel = new HorizontalPanel ();
  linksPanel.setHorizontalAlignment (HorizontalPanel.ALIGN_LEFT);
  linksPanel.setSpacing (3);
  panel.add (linksPanel, DockPanel.SOUTH);
  if (this.getBreadcrumbLinks () != null)
  {
   List<BreadcrumbLink> list = this.getBreadcrumbLinks ();
   if (list.size () > 10)
   {
    for (BreadcrumbLink link: list.subList (0, 3))
    {
     link.init (this);
     linksPanel.add ((Widget) link.getImpl ());
    }
    Label elipses = new Label ();
    elipses.setText ("...");
    linksPanel.add (elipses);
    list = list.subList (list.size () - 7, list.size ());
   }
   for (BreadcrumbLink link: list)
   {
    link.init (this);
    linksPanel.add ((Widget) link.getImpl ());
   }
  }
 }

 @Override
 public void show ()
 {
  panel.setVisible (true);
 }


	private List<BreadcrumbLink> breadcrumbLinks;

	/**
	* Set breadcrumbLinks
	*
	* optional
	*
	* The trail of links showing how the user got to where they are
	*/
	@Override
	public void setBreadcrumbLinks (List<BreadcrumbLink> breadcrumbLinks)
	{
		this.breadcrumbLinks = breadcrumbLinks;
	}

	/**
	* Get breadcrumbLinks
	*
	* optional
	*
	* The trail of links showing how the user got to where they are
	*/
	@Override
	public List<BreadcrumbLink> getBreadcrumbLinks ()
	{
		return this.breadcrumbLinks;
	}

}

