/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import org.sushik.client.component.infc.base.ui.MainMenuButton;
import org.sushik.client.component.infc.base.ui.NestedComponent;


/**
 * A navigation button on a menu where the user chooses what they want to do next
 *
 * Configured as follows:
 *
 * style - style to apply
 *     main.menu.button
 *
 * helpStyle - Style to use when displaying the help text
 *     main.menu.button.help
 *
 */
public class MainMenuButtonImpl extends NextPageLinkImpl
  implements MainMenuButton
{

 public MainMenuButtonImpl ()
 {
  super ();
 }

 private void log (String message)
 {
//  System.out.println (" " + this.getClass ().getName () + ": " + message);
 }

 @Override
 public void click ()
 {
  log ("Simulating a click on the button");
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

 public HandlerRegistration getHandlerRegistration ()
 {
  return this.handlerRegistration;
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  this.button = new Button ();
  button.setText (this.getText ());
  log ("Adding GwtClickHandler to the button");
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

