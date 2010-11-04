/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import org.sushik.client.component.infc.base.ui.EnumerationBackedTableDataSource;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.search.api.Result;
import org.sushik.client.search.api.SearchTypeInfo;


/**
* An enumeration backed source for tabular data
*
* Configured as follows:
*
* codeHeader - Typically coincides with a code representation. Likely the key if 
* this is a reference to another object.
*     Code
*
* abbrevValueHeader - Typically coincides with a shortened name. May be equal to 
* the code or value fields.
*     Abbreviated Value
*
* valueHeader - Typically coincides with a name for display.
*     Value
*
* effectiveDateHeader - Date that this enumerated value became effective. If 
* referring to another object, this may correspond with the effective date, 
* created date, date of a state transition, or some arbitrarily defined date. For 
* code/value pairs with no dates, the current date may be returned.
*     Effective Date
*
* expirationDateHeader - Date that this enumerated value expires. If referring to 
* another object, this may correspond with the expiration date, date of a state 
* transition, or some arbitrarily defined date. If this field is not specified, 
* then no expiration date has been currently defined. For code/value pairs with no 
* dates, this date may not be specified.
*     Expiration Date
*
* sortKeyHeader - Default position for the enumerated value. This might or might 
* not exist, particularly in cases where the enumeration consists solely of a 
* view.
*     Sort key
*
* contextsHeader - Indicates which context types and values this particular 
* enumerated value participates in.
*     Applicable contexts
*
* metaInfoHeader - Create and last update info for the structure. This is optional 
* and treated as read only since the data is set by the internals of the service 
* during maintenance operations.
*     Create/Update meta info
*
* typeHeader - key indicating what type of enumeration this is
*     Enumeration Type
*
* idHeader - unique id identifying this enumeration value
*     Unique id
*
*/
public class EnumerationBackedTableDataSourceImpl extends TableDataSourceImpl
 implements EnumerationBackedTableDataSource
{
	
	public EnumerationBackedTableDataSourceImpl ()
	{
		super ();
	}

	private String enumerationType;

	/**
	* Set enumerationType
	*
	* required
	*
	* The type of the enumeration
	*/
	@Override
	public void setEnumerationType (String enumerationType)
	{
		this.enumerationType = enumerationType;
	}

	/**
	* Get enumerationType
	*
	* required
	*
	* The type of the enumeration
	*/
	@Override
	public String getEnumerationType ()
	{
		return this.enumerationType;
	}


	private String contextType;

	/**
	* Set contextType
	*
	* optional
	*
	* The context supplied to get the list of of the enumeration
	*/
	@Override
	public void setContextType (String contextType)
	{
		this.contextType = contextType;
	}

	/**
	* Get contextType
	*
	* optional
	*
	* The context supplied to get the list of of the enumeration
	*/
	@Override
	public String getContextType ()
	{
		return this.contextType;
	}


	private String contextValue;

	/**
	* Set contextValue
	*
	* optional
	*
	* The value of the context to be supplied to get the list of enumerated values
	*/
	@Override
	public void setContextValue (String contextValue)
	{
		this.contextValue = contextValue;
	}

	/**
	* Get contextValue
	*
	* optional
	*
	* The value of the context to be supplied to get the list of enumerated values
	*/
	@Override
	public String getContextValue ()
	{
		return this.contextValue;
	}


	private String contextValueDate;

	/**
	* Set contextValueDate
	*
	* optional
	*
	* An additional date value to be supplied as context to get the list of
	* enumerated values
	*/
	@Override
	public void setContextValueDate (String contextValueDate)
	{
		this.contextValueDate = contextValueDate;
	}

	/**
	* Get contextValueDate
	*
	* optional
	*
	* An additional date value to be supplied as context to get the list of
	* enumerated values
	*/
	@Override
	public String getContextValueDate ()
	{
		return this.contextValueDate;
	}


	private String codeHeader;

	/**
	* Set codeHeader
	*
	* optional
	*
	* Default Value:
	* Code
	*
	* Typically coincides with a code representation. Likely the key if this is a
	* reference to another object.
	*/
	@Override
	public void setCodeHeader (String codeHeader)
	{
		this.codeHeader = codeHeader;
	}

	/**
	* Get codeHeader
	*
	* optional
	*
	* Typically coincides with a code representation. Likely the key if this is a
	* reference to another object.
	*/
	@Override
	public String getCodeHeader ()
	{
		return this.codeHeader;
	}


	private String abbrevValueHeader;

	/**
	* Set abbrevValueHeader
	*
	* optional
	*
	* Default Value:
	* Abbreviated Value
	*
	* Typically coincides with a shortened name. May be equal to the code or value
	* fields.
	*/
	@Override
	public void setAbbrevValueHeader (String abbrevValueHeader)
	{
		this.abbrevValueHeader = abbrevValueHeader;
	}

	/**
	* Get abbrevValueHeader
	*
	* optional
	*
	* Typically coincides with a shortened name. May be equal to the code or value
	* fields.
	*/
	@Override
	public String getAbbrevValueHeader ()
	{
		return this.abbrevValueHeader;
	}


	private String valueHeader;

	/**
	* Set valueHeader
	*
	* optional
	*
	* Default Value:
	* Value
	*
	* Typically coincides with a name for display.
	*/
	@Override
	public void setValueHeader (String valueHeader)
	{
		this.valueHeader = valueHeader;
	}

	/**
	* Get valueHeader
	*
	* optional
	*
	* Typically coincides with a name for display.
	*/
	@Override
	public String getValueHeader ()
	{
		return this.valueHeader;
	}


	private String effectiveDateHeader;

	/**
	* Set effectiveDateHeader
	*
	* optional
	*
	* Default Value:
	* Effective Date
	*
	* Date that this enumerated value became effective. If referring to another
	* object, this may correspond with the effective date, created date, date of a
	* state transition, or some arbitrarily defined date. For code/value pairs with
	* no dates, the current date may be returned.
	*/
	@Override
	public void setEffectiveDateHeader (String effectiveDateHeader)
	{
		this.effectiveDateHeader = effectiveDateHeader;
	}

	/**
	* Get effectiveDateHeader
	*
	* optional
	*
	* Date that this enumerated value became effective. If referring to another
	* object, this may correspond with the effective date, created date, date of a
	* state transition, or some arbitrarily defined date. For code/value pairs with
	* no dates, the current date may be returned.
	*/
	@Override
	public String getEffectiveDateHeader ()
	{
		return this.effectiveDateHeader;
	}


	private String expirationDateHeader;

	/**
	* Set expirationDateHeader
	*
	* optional
	*
	* Default Value:
	* Expiration Date
	*
	* Date that this enumerated value expires. If referring to another object, this
	* may correspond with the expiration date, date of a state transition, or some
	* arbitrarily defined date. If this field is not specified, then no expiration
	* date has been currently defined. For code/value pairs with no dates, this date
	* may not be specified.
	*/
	@Override
	public void setExpirationDateHeader (String expirationDateHeader)
	{
		this.expirationDateHeader = expirationDateHeader;
	}

	/**
	* Get expirationDateHeader
	*
	* optional
	*
	* Date that this enumerated value expires. If referring to another object, this
	* may correspond with the expiration date, date of a state transition, or some
	* arbitrarily defined date. If this field is not specified, then no expiration
	* date has been currently defined. For code/value pairs with no dates, this date
	* may not be specified.
	*/
	@Override
	public String getExpirationDateHeader ()
	{
		return this.expirationDateHeader;
	}


	private String sortKeyHeader;

	/**
	* Set sortKeyHeader
	*
	* optional
	*
	* Default Value:
	* Sort key
	*
	* Default position for the enumerated value. This might or might not exist,
	* particularly in cases where the enumeration consists solely of a view.
	*/
	@Override
	public void setSortKeyHeader (String sortKeyHeader)
	{
		this.sortKeyHeader = sortKeyHeader;
	}

	/**
	* Get sortKeyHeader
	*
	* optional
	*
	* Default position for the enumerated value. This might or might not exist,
	* particularly in cases where the enumeration consists solely of a view.
	*/
	@Override
	public String getSortKeyHeader ()
	{
		return this.sortKeyHeader;
	}


	private String contextsHeader;

	/**
	* Set contextsHeader
	*
	* optional
	*
	* Default Value:
	* Applicable contexts
	*
	* Indicates which context types and values this particular enumerated value
	* participates in.
	*/
	@Override
	public void setContextsHeader (String contextsHeader)
	{
		this.contextsHeader = contextsHeader;
	}

	/**
	* Get contextsHeader
	*
	* optional
	*
	* Indicates which context types and values this particular enumerated value
	* participates in.
	*/
	@Override
	public String getContextsHeader ()
	{
		return this.contextsHeader;
	}


	private String metaInfoHeader;

	/**
	* Set metaInfoHeader
	*
	* optional
	*
	* Default Value:
	* Create/Update meta info
	*
	* Create and last update info for the structure. This is optional and treated as
	* read only since the data is set by the internals of the service during
	* maintenance operations.
	*/
	@Override
	public void setMetaInfoHeader (String metaInfoHeader)
	{
		this.metaInfoHeader = metaInfoHeader;
	}

	/**
	* Get metaInfoHeader
	*
	* optional
	*
	* Create and last update info for the structure. This is optional and treated as
	* read only since the data is set by the internals of the service during
	* maintenance operations.
	*/
	@Override
	public String getMetaInfoHeader ()
	{
		return this.metaInfoHeader;
	}


	private String typeHeader;

	/**
	* Set typeHeader
	*
	* optional
	*
	* Default Value:
	* Enumeration Type
	*
	* key indicating what type of enumeration this is
	*/
	@Override
	public void setTypeHeader (String typeHeader)
	{
		this.typeHeader = typeHeader;
	}

	/**
	* Get typeHeader
	*
	* optional
	*
	* key indicating what type of enumeration this is
	*/
	@Override
	public String getTypeHeader ()
	{
		return this.typeHeader;
	}


	private String idHeader;

	/**
	* Set idHeader
	*
	* optional
	*
	* Default Value:
	* Unique id
	*
	* unique id identifying this enumeration value
	*/
	@Override
	public void setIdHeader (String idHeader)
	{
		this.idHeader = idHeader;
	}

	/**
	* Get idHeader
	*
	* optional
	*
	* unique id identifying this enumeration value
	*/
	@Override
	public String getIdHeader ()
	{
		return this.idHeader;
	}

	
}

