/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.BreadcrumbLink;



/**
* A navigation item that brings the user back to a previous point in the 
* navigation trail
*
* Configured as follows:
*
* style - Name of the page to which this link routes to
*     breadcrumb.link
*
* helpStyle - Style to use when displaying the help text
*     breadcrumb.link.help
*
*/
public class BreadcrumbLinkImpl extends NextPageLinkImpl
 implements BreadcrumbLink
{
	
	public BreadcrumbLinkImpl ()
	{
		super ();
	}

}

