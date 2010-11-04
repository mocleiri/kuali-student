/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.RepeatingFieldGroup;


/**
* An abstract group of fields that repeats
*
* Configured as follows:
*
* style - style for the label associated with this field group
*     field.group
*
*/
public class RepeatingFieldGroupImpl extends FieldGroupImpl
 implements RepeatingFieldGroup
{
	
	public RepeatingFieldGroupImpl ()
	{
		super ();
	}

 private String maxToShow;

	/**
	* Set maxToShow
	*
	* optional
	*
	* The maximum number of repeats to show on the screen
	*/
	@Override
	public void setMaxToShow (String maxToShow)
	{
		this.maxToShow = maxToShow;
	}

	/**
	* Get maxToShow
	*
	* optional
	*
	* The maximum number of repeats to show on the screen
	*/
	@Override
	public String getMaxToShow ()
	{
		return this.maxToShow;
	}


}

