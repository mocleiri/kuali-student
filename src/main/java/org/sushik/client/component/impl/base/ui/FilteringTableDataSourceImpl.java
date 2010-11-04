/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import java.util.List;
import org.sushik.client.component.infc.base.ui.FilteringTableDataSource;
import org.sushik.client.component.infc.base.ui.TableDataSource;
import org.sushik.client.component.infc.base.ui.TableRowFilter;
import org.sushik.client.component.infc.base.ui.TableRowFilterAsync;


/**
* A link that is displayed in the header band
*
* Configured as follows:
*
* style - Name of the page to which this link routes to
*     header.link
*
* helpStyle - Style to use when displaying the help text
*     header.link.help
*
*/
public class FilteringTableDataSourceImpl extends TableDataSourceImpl
 implements FilteringTableDataSource
{
	
	public FilteringTableDataSourceImpl ()
	{
		super ();
	}

 @Override
 public List<TableRowFilterAsync> getAsynchRowFilters ()
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }

 @Override
 public List<TableRowFilter> getRowFilters ()
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }

 @Override
 public List<TableDataSource> getTableData ()
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }

 @Override
 public void setAsynchRowFilters (List<TableRowFilterAsync> asynchRowFilters)
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }

 @Override
 public void setRowFilters (List<TableRowFilter> rowFilters)
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }

 @Override
 public void setTableData (List<TableDataSource> tableData)
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }


}

