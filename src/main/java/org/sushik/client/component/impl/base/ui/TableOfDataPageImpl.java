/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.TableDataSection;
import org.sushik.client.component.infc.base.ui.TableOfDataPage;

/**
 * A page that contains a table of data
 *
 * Configured as follows:
 *
 */
public class TableOfDataPageImpl extends StandardPageImpl
  implements TableOfDataPage
{

 public TableOfDataPageImpl ()
 {
  super ();
 }
 private TableDataSection tableDataSection;

 @Override
 public TableDataSection getTableDataSection ()
 {
  return tableDataSection;
 }

 @Override
 public void setTableDataSection (TableDataSection tableDataSection)
 {
  this.tableDataSection = tableDataSection;
 }

 @Override
 public void init (NestedComponent parent)
 {
  super.init (parent);
  DockPanel page = (DockPanel) this.getImpl ();
  if (this.getTableDataSection () != null)
  {
   this.getTableDataSection ().init (this);
   page.add ((Widget) this.getTableDataSection ().getImpl (), DockPanel.CENTER);
  }
 }
}

