/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import java.util.Map;
import org.sushik.client.component.infc.base.ui.BoundField;




/**
* band
*/
public class BoundFieldImpl extends FieldImpl
 implements BoundField
{
	
	public BoundFieldImpl ()
	{
		super ();
	}
	
		private String objectKey;

	/**
	* Set objectKey
	*
	* required
	*
	* Key of the object structure to which this is bound, i.e. cluInfo.  The fieldKey
	* should then be the dot separated path to the field within the object
	*/
	@Override
	public void setObjectKey (String objectKey)
	{
		this.objectKey = objectKey;
	}

	/**
	* Get objectKey
	*
	* required
	*
	* Key of the object structure to which this is bound, i.e. cluInfo.  The fieldKey
	* should then be the dot separated path to the field within the object
	*/
	@Override
	public String getObjectKey ()
	{
		return this.objectKey;
	}


	private String mainObjectTypeKey;

	/**
	* Set mainObjectTypeKey
	*
	* optional
	*
	* Type key of the main object that is bound
	*/
	@Override
	public void setMainObjectTypeKey (String mainObjectTypeKey)
	{
		this.mainObjectTypeKey = mainObjectTypeKey;
	}

	/**
	* Get mainObjectTypeKey
	*
	* optional
	*
	* Type key of the main object that is bound
	*/
	@Override
	public String getMainObjectTypeKey ()
	{
		return this.mainObjectTypeKey;
	}


	private String mainObjectStateKey;

	/**
	* Set mainObjectStateKey
	*
	* optional
	*
	* State key of the main object that is bound
	*/
	@Override
	public void setMainObjectStateKey (String mainObjectStateKey)
	{
		this.mainObjectStateKey = mainObjectStateKey;
	}

	/**
	* Get mainObjectStateKey
	*
	* optional
	*
	* State key of the main object that is bound
	*/
	@Override
	public String getMainObjectStateKey ()
	{
		return this.mainObjectStateKey;
	}


	private String subObjectTypeKey;

	/**
	* Set subObjectTypeKey
	*
	* optional
	*
	* Type key of the main object that is bound
	*/
	@Override
	public void setSubObjectTypeKey (String subObjectTypeKey)
	{
		this.subObjectTypeKey = subObjectTypeKey;
	}

	/**
	* Get subObjectTypeKey
	*
	* optional
	*
	* Type key of the main object that is bound
	*/
	@Override
	public String getSubObjectTypeKey ()
	{
		return this.subObjectTypeKey;
	}


	private String subObjectStateKey;

	/**
	* Set subObjectStateKey
	*
	* optional
	*
	* State key of the main object that is bound
	*/
	@Override
	public void setSubObjectStateKey (String subObjectStateKey)
	{
		this.subObjectStateKey = subObjectStateKey;
	}

	/**
	* Get subObjectStateKey
	*
	* optional
	*
	* State key of the main object that is bound
	*/
	@Override
	public String getSubObjectStateKey ()
	{
		return this.subObjectStateKey;
	}


	private Object boundRootObject;

	/**
	* Set boundRootObject
	*
	* optional
	*
	* The root object that holds the datta field to which this screen field is is
	* currently bound
	*/
	@Override
	public void setBoundRootObject (Object boundRootObject)
	{
		this.boundRootObject = boundRootObject;
	}

	/**
	* Get boundRootObject
	*
	* optional
	*
	* The root object that holds the datta field to which this screen field is is
	* currently bound
	*/
	@Override
	public Object getBoundRootObject ()
	{
		return this.boundRootObject;
	}


	private Object boundSubObject;

	/**
	* Set boundSubObject
	*
	* optional
	*
	* The immediate object that holds the datta field to which this screen field is
	* is currently bound
	*/
	@Override
	public void setBoundSubObject (Object boundSubObject)
	{
		this.boundSubObject = boundSubObject;
	}

	/**
	* Get boundSubObject
	*
	* optional
	*
	* The immediate object that holds the datta field to which this screen field is
	* is currently bound
	*/
	@Override
	public Object getBoundSubObject ()
	{
		return this.boundSubObject;
	}


	private Map<String, String> bindContext;

	/**
	* Set bindContext
	*
	* optional
	*
	* Bind the field to an info object using the context to identify the object or
	* occurrence of a substructure within the object as
	* needed.
	* The keys can be interpreted as ID's, typeKeys or occurrence numbers as needed
	*/
	@Override
	public void setBindContext (Map<String, String> bindContext)
	{
		this.bindContext = bindContext;
	}

	/**
	* Get bindContext
	*
	* optional
	*
	* Bind the field to an info object using the context to identify the object or
	* occurrence of a substructure within the object as
	* needed.
	* The keys can be interpreted as ID's, typeKeys or occurrence numbers as needed
	*/
	@Override
	public Map<String, String> getBindContext ()
	{
		return this.bindContext;
	}


	/**
	* Load the bound value from the data structure into the field
	*/
 @Override
	public void loadBoundValue ()
 {
   throw new UnsupportedOperationException ("Not supported yet.");
 }

	/**
	* Store the bound value into the waiting data structure
	*/
 @Override
	public void storeBoundValue ()
 {
  throw new UnsupportedOperationException ("Not supported yet.");
 }


}

