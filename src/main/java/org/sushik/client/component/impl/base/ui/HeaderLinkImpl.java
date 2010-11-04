/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.HeaderLink;


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
public class HeaderLinkImpl extends NextPageLinkImpl
 implements HeaderLink
{
	
	public HeaderLinkImpl ()
	{
		super ();
	}
	
}

