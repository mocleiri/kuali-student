/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.PageFooter;
import org.sushik.client.component.infc.base.ui.PageHeader;
import org.sushik.client.component.infc.base.ui.PopupPage;

/**
 * A popup page
 *
 */
public class PopupPageImpl extends PageImpl
  implements PopupPage
{

 public PopupPageImpl ()
 {
  super ();
 }

 	private PageHeader pageHeader;

	/**
	* Set pageHeader
	*
	* required
	*
	* Default Value:
	* Standard Popup Header
	*
	* Title of the popup
	*/
	@Override
	public void setPageHeader (PageHeader pageHeader)
	{
		this.pageHeader = pageHeader;
	}

	/**
	* Get pageHeader
	*
	* required
	*
	* Title of the popup
	*/
	@Override
	public PageHeader getPageHeader ()
	{
		return this.pageHeader;
	}


	private PageFooter pageFooter;

	/**
	* Set pageFooter
	*
	* required
	*
	* Default Value:
	* Standard Page Footer
	*
	* Title of the popup
	*/
	@Override
	public void setPageFooter (PageFooter pageFooter)
	{
		this.pageFooter = pageFooter;
	}

	/**
	* Get pageFooter
	*
	* required
	*
	* Title of the popup
	*/
	@Override
	public PageFooter getPageFooter ()
	{
		return this.pageFooter;
	}
	

}

