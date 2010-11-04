/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.ToolbarButton;


/**
 * A navigation link represented as a button on a toolbar where the user chooses
 * what they want to do next
 *
 * Configured as follows:
 *
 * style - Name of the page to which this link routes to
 *     toolbar.button
 *
 * helpStyle - Style to use when displaying the help text
 *     toolbar.button.help
 *
 */
public class ToolbarButtonImpl extends NextPageLinkWithCheckingImpl
  implements ToolbarButton
{

 public ToolbarButtonImpl ()
 {
  super ();
 }

 @Override
 public void click ()
 {
  button.click ();
 }
 private Button button;

 @Override
 public Object getImpl ()
 {
  return button;
 }

 @Override
 public void hide ()
 {
  button.setVisible (false);
 }
 private HandlerRegistration handlerRegistration;
 private GwtClickHandler gwtClickHandler;

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  this.button = new Button ();
  button.setText (this.getText ());
  button.setEnabled (false);
  gwtClickHandler = new GwtClickHandler (this);
  handlerRegistration = button.addClickHandler (gwtClickHandler);
 }

 @Override
 public void show ()
 {
  button.setVisible (true);
 }

 @Override
 public boolean isEnabled ()
 {
  return button.isEnabled ();
 }

 @Override
 public void setEnabled (boolean enabled)
 {
  button.setEnabled (enabled);
  return;
 }
}

