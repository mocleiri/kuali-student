/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.Page;
import org.sushik.client.component.infc.base.ui.TabSectionHeader;


/**
 * A navigational link that presents the choice in the form of tabs across the page
 *
 * Configured as follows:
 *
 * style - Name of the page to which this link routes to
 *     tab.section.header
 *
 * helpStyle - Style to use when displaying the help text
 *     tab.section.header.help
 *
 */
public class TabSectionHeaderImpl extends NextPageLinkImpl
    implements TabSectionHeader
{

 public TabSectionHeaderImpl ()
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

 
 private Page getPage (NestedComponent component)
 {
  if (component == null)
  {
   return null;
  }
  if (component instanceof Page)
  {
   return (Page) component;
  }
  return getPage (component.getParent ());
 }

 private HandlerRegistration handlerRegistration;

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  this.button = new Button ();
  button.setEnabled (false);
  button.setText (this.getText ());
  handlerRegistration =
  button.addClickHandler (new GwtClickHandler (this));
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

