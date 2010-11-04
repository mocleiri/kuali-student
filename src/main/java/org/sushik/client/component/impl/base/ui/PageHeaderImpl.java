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
import org.sushik.client.component.infc.base.ui.Breadcrumbs;
import org.sushik.client.component.infc.base.ui.HeaderLinks;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.PageHeader;

/**
 * A section at the top of a page
 *
 * Configured as follows:
 *
 * style - Links displayed as part of the header
 *     page.title
 *
 */
public class PageHeaderImpl extends BandImpl
  implements PageHeader
{

 public PageHeaderImpl ()
 {
  super ();
 }
 private DockPanel header;

 @Override
 public Object getImpl ()
 {
  return header;
 }

 @Override
 public void hide ()
 {
  header.setVisible (false);
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  header = new DockPanel ();
  header.setWidth ("100%");
  header.setBorderWidth (1);
  if (this.getText () != null)
  {
   HorizontalPanel panel = new HorizontalPanel ();
   panel.setWidth ("100%");
   panel.setHorizontalAlignment (HorizontalPanel.ALIGN_CENTER);
   header.add (panel, DockPanel.NORTH);
   Label title = new Label ();
   title.setText (this.getText ());
   panel.add (title);
  }
  if (this.getHeaderLinks () != null)
  {
   this.getHeaderLinks ().init (this);
   header.add ((Widget) getHeaderLinks ().getImpl (), DockPanel.NORTH);
  }
  if (this.getBreadcrumbs () != null)
  {
   this.getBreadcrumbs ().init (this);
   header.add ((Widget) getBreadcrumbs ().getImpl (), DockPanel.SOUTH);
  }
  if (this.getContextTitle () != null)
  {
   HorizontalPanel panel = new HorizontalPanel ();
   panel.setWidth ("100%");
   panel.setHorizontalAlignment (HorizontalPanel.ALIGN_CENTER);
   header.add (panel, DockPanel.SOUTH);
   Label title = new Label ();
   title.setText (this.getContextTitle ());
   panel.add (title);
  }
 }

 @Override
 public void show ()
 {
  header.setVisible (true);
 }
 private String text;

 /**
  * Set text
  *
  * required
  *
  * The title of the page
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
  * The title of the page
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
  * page-title
  *
  * The style for the page title
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
  * The style for the page title
  */
 @Override
 public String getStyle ()
 {
  return this.style;
 }
 private HeaderLinks headerLinks;

 /**
  * Set headerLinks
  *
  * required
  *
  * Links displayed as part of the header
  */
 @Override
 public void setHeaderLinks (HeaderLinks headerLinks)
 {
  this.headerLinks = headerLinks;
 }

 /**
  * Get headerLinks
  *
  * required
  *
  * Links displayed as part of the header
  */
 @Override
 public HeaderLinks getHeaderLinks ()
 {
  return this.headerLinks;
 }
 private Breadcrumbs breadcrumbs;

 /**
  * Set breadcrumbs
  *
  * optional
  *
  * Links displayed as a breadcrumb trail showing how the user navigated to this
  * point
  */
 @Override
 public void setBreadcrumbs (Breadcrumbs breadcrumbs)
 {
  this.breadcrumbs = breadcrumbs;
 }

 /**
  * Get breadcrumbs
  *
  * optional
  *
  * Links displayed as a breadcrumb trail showing how the user navigated to this
  * point
  */
 @Override
 public Breadcrumbs getBreadcrumbs ()
 {
  return this.breadcrumbs;
 }
 private String contextTitle;

 /**
  * Set contextTitle
  *
  * optional
  *
  * Optional title to be displayed to show the context of the page.  For example
  * the title of the proposal being edited
  */
 @Override
 public void setContextTitle (String contextTitle)
 {
  this.contextTitle = contextTitle;
 }

 /**
  * Get contextTitle
  *
  * optional
  *
  * Optional title to be displayed to show the context of the page.  For example
  * the title of the proposal being edited
  */
 @Override
 public String getContextTitle ()
 {
  return this.contextTitle;
 }
}

