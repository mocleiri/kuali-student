/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import org.sushik.client.component.infc.base.ui.Link;

/**
 *
 * @author nwright
 */
public class GwtClickHandler implements ClickHandler
{


 private Link link;

 public GwtClickHandler (Link link)
 {
  this.link = link;
 }


 @Override
 public void onClick (ClickEvent event)
 {
  if ( ! link.isEnabled ())
  {
   return;
  }
  // TODO: do the click
 }


}
