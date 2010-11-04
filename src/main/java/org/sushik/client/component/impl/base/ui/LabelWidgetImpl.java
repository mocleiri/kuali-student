/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.LabelWidget;


/**
* An implementation of a label as a widget
*
* Configured as follows:
*
* size - Size of the text box
*     30
*
* readOnly - Labels are always read only
*     true
*
*/
public class LabelWidgetImpl extends WidgetImpl
 implements LabelWidget
{
	
	public LabelWidgetImpl ()
	{
		super ();
	}


	private Integer size;

	/**
	* Set size
	*
	* required
	*
	* Default Value:
	* 30
	*
	* Size of the text box
	*/
	@Override
	public void setSize (Integer size)
	{
		this.size = size;
	}

	/**
	* Get size
	*
	* required
	*
	* Size of the text box
	*/
	@Override
	public Integer getSize ()
	{
		return this.size;
	}


	private Boolean readOnly;

	/**
	* Set readOnly
	*
	* required
	*
	* Default Value:
	* true
	*
	* Labels are always read only
	*/
	@Override
	public void setReadOnly (Boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	/**
	* Get readOnly
	*
	* required
	*
	* Labels are always read only
	*/
	@Override
	public Boolean isReadOnly ()
	{
		return this.readOnly;
	}
	
}

