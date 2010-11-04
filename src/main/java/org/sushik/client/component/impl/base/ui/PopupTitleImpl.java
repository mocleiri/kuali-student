/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.BrowserComponent;
import org.sushik.client.component.infc.base.ui.PopupTitle;

/**
 * A title for a Poplup Window
 *
 * Configured as follows:
 *
 */
public class PopupTitleImpl extends BrowserComponentImpl
  implements PopupTitle
{

 public PopupTitleImpl ()
 {
  super ();
 }
 private String text;

 @Override
 public String getText ()
 {
  return this.text;
 }

 @Override
 public void setText (String text)
 {
  this.text = text;
 }
}

