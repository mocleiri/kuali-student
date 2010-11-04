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
import org.sushik.client.component.infc.base.ui.MainMenuPage;
import org.sushik.client.component.infc.base.ui.MainMenuSection;
import org.sushik.client.component.infc.base.ui.NestedComponent;


/**
 * A page that displays a menu to the user for them to choose
 *
 * Configured as follows:
 *
 */
public class MainMenuPageImpl extends StandardPageImpl
    implements MainMenuPage
{

 public MainMenuPageImpl ()
 {
  super ();
 }



 private List<MainMenuSection> mainMenuSections;

 /**
  * Set mainMenuSections
  *
  * optional
  *
  * The section that holds the data fields
  */
 @Override
 public void setMainMenuSections (List<MainMenuSection> mainMenuSections)
 {
  this.mainMenuSections = mainMenuSections;
 }

 /**
  * Get mainMenuSections
  *
  * optional
  *
  * The section that holds the data fields
  */
 @Override
 public List<MainMenuSection> getMainMenuSections ()
 {
  return this.mainMenuSections;
 }

 @Override
 public void init (NestedComponent parent)
 {
  super.init (parent);
  DockPanel dockPanel = (DockPanel) this.getImpl ();
  VerticalPanel body = new VerticalPanel ();
  dockPanel.add (body, DockPanel.CENTER);
  if (this.getMainMenuSections () != null)
  {
   for (MainMenuSection section: getMainMenuSections ())
   {
    section.init (this);
    body.add ((Widget) section.getImpl ());
   }
  }
 }

}

