/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.impl.base.LogicImpl;
import org.sushik.client.component.infc.base.TransformLogic;


/**
* Logic that is used to transform one set of values into another set of values. 
*  Typically used in a copy operation
*
* Configured as follows:
*
*/
public class TransformLogicImpl extends LogicImpl
 implements TransformLogic
{
	
	public TransformLogicImpl ()
	{
		super ();
	}
	
	/**
	* Set Transforms the object in some fashion
	*/
 @Override
	public Object transform (Object value)
	{
		throw new UnsupportedOperationException ();
	}
	
}

