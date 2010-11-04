/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import java.util.List;
import org.sushik.client.component.infc.base.ui.SelectListWidget;



/**
* An implementation of a select list (also called drop down list or combo box) as 
* a widget
*
* Configured as follows:
*
*/
public class SelectListWidgetImpl extends WidgetImpl
 implements SelectListWidget
{
	
	public SelectListWidgetImpl ()
	{
		super ();
	}


	private List<String> code;

	/**
	* Set code
	*
	* required
	*
	* The values to be selected
	*/
	@Override
	public void setCode (List<String> code)
	{
		this.code = code;
	}

	/**
	* Get code
	*
	* required
	*
	* The values to be selected
	*/
	@Override
	public List<String> getCode ()
	{
		return this.code;
	}


	private List<String> description;

	/**
	* Set description
	*
	* optional
	*
	* The description of the value to be selected
	*/
	@Override
	public void setDescription (List<String> description)
	{
		this.description = description;
	}

	/**
	* Get description
	*
	* optional
	*
	* The description of the value to be selected
	*/
	@Override
	public List<String> getDescription ()
	{
		return this.description;
	}
	
}

