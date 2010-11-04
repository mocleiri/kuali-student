/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.BackButton;




/**
* The built-in navigation item where the user clicks on the user indicates they 
* want to go back to the previous page
*
* Configured as follows:
*
*/
public class BackButtonImpl extends BrowserComponentImpl
 implements BackButton
{
	
	public BackButtonImpl ()
	{
		super ();
	}
	
	/**
	* Set Executes logic when the back button is 
	* @return false if should stop the user from moving to the previous page
	*/
 @Override
	public boolean onBackButton ()
	{
		throw new UnsupportedOperationException ();
	}
	
}

