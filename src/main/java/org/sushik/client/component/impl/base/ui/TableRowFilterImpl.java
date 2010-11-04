/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.impl.base.FilterLogicImpl;
import org.sushik.client.component.infc.base.ui.TableRowFilter;
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
public class TableRowFilterImpl extends FilterLogicImpl
 implements TableRowFilter
{
	
	public TableRowFilterImpl ()
	{
		super ();
	}

 @Override
 public boolean accept (Result result)
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }

}

