/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sushik.client.component.impl.base.FilterLogicImpl;
import org.sushik.client.component.infc.base.ui.TableRowFilterAsync;
import org.sushik.client.search.api.Result;


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
public class TableRowFilterAsyncImpl extends FilterLogicImpl
 implements TableRowFilterAsync
{
	
	public TableRowFilterAsyncImpl ()
	{
		super ();
	}

 @Override
 public void getResults (Result result,
                         AsyncCallback<Boolean> callback)
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }



}

