/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.DataEntrySection;
import org.sushik.client.component.infc.base.ui.NestedComponent;


/**
 *
 * @author nwright
 */
public class DataEntrySectionHelper
{

 public DataEntrySection getDataEntrySection (NestedComponent component)
 {
  if (component == null)
  {
   return null;
  }
  if (component instanceof DataEntrySection)
  {
   return (DataEntrySection) component;
  }
  return getDataEntrySection (component.getParent ());
 }

}
