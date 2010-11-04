/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sushik.client.component.infc.base.ui.ContextToolbar;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.ViewDataPage;
import org.sushik.client.component.infc.base.ui.ViewDataSection;


/**
 * A pages that accepts data entry input from the user
 *
 * Configured as follows:
 *
 */
public class ViewDataPageImpl  extends StandardPageImpl
    implements ViewDataPage
{

 public ViewDataPageImpl ()
 {
  super ();
 }

 private List<ViewDataSection> viewDataSections;

 @Override
 public List<ViewDataSection> getViewDataSections ()
 {
  return viewDataSections;
 }

 @Override
 public void setViewDataSections (List<ViewDataSection> viewDataSections)
 {
  this.viewDataSections = viewDataSections;
 }

 private ContextToolbar toolbar;

 public ContextToolbar getToolbar ()
 {
  return toolbar;
 }

 public void setToolbar (ContextToolbar toolbar)
 {
  this.toolbar = toolbar;
 }



 @Override
 public void init (NestedComponent parent)
 {
  super.init (parent);
  DockPanel page = (DockPanel) this.getImpl ();
  VerticalPanel panel = new VerticalPanel ();
  page.add (panel, DockPanel.CENTER);
  if (this.getToolbar () != null)
  {
   this.getToolbar ().init (this);
   panel.add ((Widget) this.getToolbar ().getImpl ());
  }
  if (this.getViewDataSections () != null)
  {
   for (ViewDataSection section: this.getViewDataSections ())
   {
    section.init (this);
    panel.add ((Widget) section.getImpl ());
   }
  }
 }

}

