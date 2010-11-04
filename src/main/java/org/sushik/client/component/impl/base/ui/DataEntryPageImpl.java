/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;
import org.sushik.client.component.infc.base.ui.DataEntryPage;
import org.sushik.client.component.infc.base.ui.DataEntrySection;
import org.sushik.client.component.infc.base.ui.NestedComponent;

/**
 * A pages that accepts data entry input from the user
 *
 * Configured as follows:
 *
 */
public class DataEntryPageImpl extends StandardPageImpl
  implements DataEntryPage
{

 public DataEntryPageImpl ()
 {
  super ();
 }
 private DataEntrySection dataEntrySection;

 @Override
 public DataEntrySection getDataEntrySection ()
 {
  return dataEntrySection;
 }

 @Override
 public void setDataEntrySection (DataEntrySection dataEntrySection)
 {
  this.dataEntrySection = dataEntrySection;
 }

 @Override
 public void init (NestedComponent parent)
 {
  super.init (parent);
  DockPanel page = (DockPanel) this.getImpl ();
  if (this.getDataEntrySection () != null)
  {
   this.getDataEntrySection ().init (this);
   page.add ((Widget) this.getDataEntrySection ().getImpl (), DockPanel.CENTER);
  }
 }
}

