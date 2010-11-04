/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.sushik.client.component.infc.base.ui.CloseWindow;
import org.sushik.client.component.infc.base.ui.ContextToolbar;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.PopupMessageDialog;
import org.sushik.client.component.infc.base.ui.PopupTitle;

/**
 * A section at the top of a page
 *
 * Configured as follows:
 *
 * style - Links displayed as part of the header
 *     page.title
 *
 */
public class PopupMessageDialogImpl extends PopupPageImpl
  implements PopupMessageDialog
{

 public PopupMessageDialogImpl ()
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
  this.panel.setVisible (false);
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  this.panel = new DockPanel ();

  if (this.getToolbar () != null)
  {
   this.getToolbar ().init (this);
   panel.add ((Widget) this.getToolbar (), DockPanel.CENTER);
  }
  if (this.getMessageStyle () != null)
  {
   Label label = new Label ();
   label.setText (this.getMessageText ());
   panel.add (label, DockPanel.CENTER);
  }
 }

 @Override
 public void show ()
 {
  this.panel.setVisible (true);
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
 private PopupTitle popupTitle;

 /**
  * Set popupTitle
  *
  * required
  *
  * Title of the popup
  */
 @Override
 public void setPopupTitle (PopupTitle popupTitle)
 {
  this.popupTitle = popupTitle;
 }

 /**
  * Get popupTitle
  *
  * required
  *
  * Title of the popup
  */
 @Override
 public PopupTitle getPopupTitle ()
 {
  return this.popupTitle;
 }
 private String messageText;

 /**
  * Set messageText
  *
  * required
  *
  * Text of message to be displayed to the user
  */
 @Override
 public void setMessageText (String messageText)
 {
  this.messageText = messageText;
 }

 /**
  * Get messageText
  *
  * required
  *
  * Text of message to be displayed to the user
  */
 @Override
 public String getMessageText ()
 {
  return this.messageText;
 }
 private String messageStyle;

 /**
  * Set messageStyle
  *
  * required
  *
  * Default Value:
  * message
  *
  * Style of message to be displayed to the user
  */
 @Override
 public void setMessageStyle (String messageStyle)
 {
  this.messageStyle = messageStyle;
 }

 /**
  * Get messageStyle
  *
  * required
  *
  * Style of message to be displayed to the user
  */
 @Override
 public String getMessageStyle ()
 {
  return this.messageStyle;
 }
 private ContextToolbar toolbar;

 /**
  * Set toolbar
  *
  * required
  *
  * Bar of buttons for user to navigate
  */
 @Override
 public void setToolbar (ContextToolbar toolbar)
 {
  this.toolbar = toolbar;
 }

 /**
  * Get toolbar
  *
  * required
  *
  * Bar of buttons for user to navigate
  */
 @Override
 public ContextToolbar getToolbar ()
 {
  return this.toolbar;
 }
}

