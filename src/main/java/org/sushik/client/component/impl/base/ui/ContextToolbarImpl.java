/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sushik.client.component.infc.base.ui.ContextToolbar;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.ToolbarButton;

/**
 * A toolbar that changes depending on the context
 *
 * Configured as follows:
 *
 */
public class ContextToolbarImpl extends BandImpl
  implements ContextToolbar
{

 public ContextToolbarImpl ()
 {
  super ();
 }
 private HorizontalPanel panel;

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
  this.panel = new HorizontalPanel ();
  this.panel.setBorderWidth (3);
  if (this.getToolbarButtons () != null)
  {
   for (ToolbarButton button : this.getToolbarButtons ())
   {
    button.init (this);
    panel.add ((Widget) button.getImpl ());
   }
  }
 }

 @Override
 public void show ()
 {
  this.panel.setVisible (true);
 }
 private List<ToolbarButton> toolbarButtons;

 /**
  * Set toolbarButtons
  *
  * required
  *
  * Buttons that should be displayed in this context
  */
 @Override
 public void setToolbarButtons (List<ToolbarButton> toolbarButtons)
 {
  this.toolbarButtons = toolbarButtons;
 }

 /**
  * Get toolbarButtons
  *
  * required
  *
  * Buttons that should be displayed in this context
  */
 @Override
 public List<ToolbarButton> getToolbarButtons ()
 {
  return this.toolbarButtons;
 }
}

