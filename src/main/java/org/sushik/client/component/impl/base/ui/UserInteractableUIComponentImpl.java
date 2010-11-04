/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.UserInteractableUIComponent;
import org.sushik.client.component.infc.base.ui.VisibleUIComponent;




/**
* band
*/
public class UserInteractableUIComponentImpl extends VisibleUIComponentImpl
 implements UserInteractableUIComponent
{
	
	public UserInteractableUIComponentImpl ()
	{
		super ();
	}

 @Override
 public boolean isEnabled ()
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }

 @Override
 public void setEnabled (boolean enabled)
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }


	
}

