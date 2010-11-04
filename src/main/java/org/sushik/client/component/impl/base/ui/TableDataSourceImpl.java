/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import java.util.List;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.Page;
import org.sushik.client.component.infc.base.ui.TableDataSource;
import org.sushik.client.search.api.Result;
import org.sushik.client.search.api.SearchTypeInfo;


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
public class TableDataSourceImpl extends NestedComponentImpl
    implements TableDataSource
{

 public TableDataSourceImpl ()
 {
  super ();
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
 }

 @Override
 public void getResults (AsyncCallback<List<Result>> callback)
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }

 @Override
 public void getSearchTypeInfo (AsyncCallback<SearchTypeInfo> callback)
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }

 
}

