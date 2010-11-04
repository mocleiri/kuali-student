/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.TextboxWidget;

/**
 * An implementation of a text box as a widget
 *
 * Configured as follows:
 *
 * size - Size of the text box
 *     30
 *
 * maxSize - Maximum size for data entry
 *     250
 *
 */
public class TextboxWidgetImpl extends WidgetImpl
  implements TextboxWidget
{

 public TextboxWidgetImpl ()
 {
  super ();
 }
 private int maxSize;

 @Override
 public Integer getMaxSize ()
 {
  return this.maxSize;
 }
 private int size;

 @Override
 public Integer getSize ()
 {
  return this.size;
 }

 @Override
 public void setMaxSize (Integer maxSize)
 {
  this.maxSize = maxSize;
 }

 @Override
 public void setSize (Integer size)
 {
  this.size = size;
 }
}

