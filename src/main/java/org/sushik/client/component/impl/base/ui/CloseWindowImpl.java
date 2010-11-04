/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;
import org.sushik.client.component.infc.base.ui.CloseWindow;


/**
* The built-in navigation item where the user closes the window, typically by 
* clicking on an X in the top right corner
*
* Configured as follows:
*
*/
public class CloseWindowImpl extends BrowserComponentImpl
 implements CloseWindow
{
	
	public CloseWindowImpl ()
	{
		super ();
	}
	

 @Override
	public boolean onWindowClose ()
	{
		throw new UnsupportedOperationException ();
	}
	
}

