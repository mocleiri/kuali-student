/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import org.sushik.client.component.infc.base.ui.Field;
import org.sushik.client.component.infc.base.ui.FieldGroup;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.SearchBackedTableDataSource;
import org.sushik.client.component.infc.base.ui.SearchCriteriaSection;
import org.sushik.client.component.infc.base.ui.TableDataSection;
import org.sushik.client.search.api.QueryParamValue;
import org.sushik.client.search.api.QueryParamValueBean;
import org.sushik.client.search.api.Result;
import org.sushik.client.search.api.SearchTypeInfo;
import org.sushik.client.search.gwt.service.api.SearchGwtServiceAsync;

/**
 * A search backed source for tabular data
 *
 * Configured as follows:
 *
 */
public class SearchBackedTableDataSourceImpl extends FilteringTableDataSourceImpl
  implements SearchBackedTableDataSource
{

 public SearchBackedTableDataSourceImpl ()
 {
  super ();
 }

 @Override
 public void getSearchTypeInfo (AsyncCallback<SearchTypeInfo> callback)
 {
  String currentUser = null;
  this.getSearchService ().getSearchType (currentUser,
                                          this.getSearchType (),
                                          callback);
 }

 @Override
 public void getResults (AsyncCallback<List<Result>> callback)
 {
  List<QueryParamValue> params = new ArrayList ();
  for (int i = 0; i < this.getSearchParameterNames ().size (); i ++)
  {
   QueryParamValue param = new QueryParamValueBean ();
   param.setKey (this.getSearchParameterNames ().get (i));
   param.setValue (this.getSearchParameterValues ().get (i));
   params.add (param);
  }
  if (this.getBindFieldKeys () != null)
  {
   this.bindFieldValues (params);
  }
  String currentUser = null;
  this.getSearchService ().searchForResults (currentUser,
                                             this.getSearchType (),
                                             params,
                                             callback);
 }

 private void bindFieldValues (List<QueryParamValue> params)
 {
  for (int i = 0; i < this.getBindFieldKeys ().size (); i ++)
  {
   String fieldKey = this.getBindFieldKeys ().get (i);
   String paramKey = this.getBindSearchParameterNames ().get (i);
   QueryParamValue param = new QueryParamValueBean ();
   param.setKey (paramKey);
   param.setValue (this.getFieldValue (fieldKey));
   params.add (param);
  }
 }

 private String getFieldValue (String fieldKey)
 {
  Field field = this.findField (fieldKey);
  if (field == null)
  {
   throw new IllegalArgumentException ("Illegal configuration - no field with the key "
                                       + fieldKey + " exists.");
  }
  String value = field.getValueString ();
  return value;
 }

 private Field findField (String fieldKey)
 {
  if ( ! (this.getParent () instanceof TableDataSection))
  {
   throw new IllegalArgumentException (
     "parent of search backed table source is not a TableDataSection");
  }
  TableDataSection tds = (TableDataSection) this.getParent ();
  SearchCriteriaSection scs = tds.getCriteria ();
  if (scs == null)
  {
   return null;
  }
  if (scs.getFieldGroups () == null)
  {
   return null;
  }
  for (FieldGroup fg : scs.getFieldGroups ())
  {
   for (Field field : fg.getFields ())
   {
    if (field.getFieldKey ().equalsIgnoreCase (fieldKey))
    {
     return field;
    }
   }
  }
  return null;
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
 }
 private String searchType;

 /**
  * Set searchType
  *
  * required
  *
  * The type of the search to be executed as defined in search specification.xls
  */
 @Override
 public void setSearchType (String searchType)
 {
  this.searchType = searchType;
 }

 /**
  * Get searchType
  *
  * required
  *
  * The type of the search to be executed as defined in search specification.xls
  */
 @Override
 public String getSearchType ()
 {
  return this.searchType;
 }
 private List<String> searchParameterNames;

 /**
  * Set searchParameterNames
  *
  * optional
  *
  * The name of the parameter - each must exactly match a search criteria parameter
  * key as defined in the search specification.xls
  */
 @Override
 public void setSearchParameterNames (List<String> searchParameterNames)
 {
  this.searchParameterNames = searchParameterNames;
 }

 /**
  * Get searchParameterNames
  *
  * optional
  *
  * The name of the parameter - each must exactly match a search criteria parameter
  * key as defined in the search specification.xls
  */
 @Override
 public List<String> getSearchParameterNames ()
 {
  return this.searchParameterNames;
 }
 private List<String> searchParameterValues;

 /**
  * Set searchParameterValues
  *
  * optional
  *
  * The value of the cooresponding parameter.  Used to configure default/hard coded
  * values to be supplied to the search in this usage context.  For example a list
  * of types is often supplied this way to restrict the result to just the type of
  * the result expected.
  */
 @Override
 public void setSearchParameterValues (List<String> searchParameterValues)
 {
  this.searchParameterValues = searchParameterValues;
 }

 /**
  * Get searchParameterValues
  *
  * optional
  *
  * The value of the cooresponding parameter.  Used to configure default/hard coded
  * values to be supplied to the search in this usage context.  For example a list
  * of types is often supplied this way to restrict the result to just the type of
  * the result expected.
  */
 @Override
 public List<String> getSearchParameterValues ()
 {
  return this.searchParameterValues;
 }
 private List<String> bindSearchParameterNames;

 /**
  * Set bindSearchParameterNames
  *
  * optional
  *
  * The corresponding names to the search criteria parameters supplying a user
  * entered value to the search
  */
 @Override
 public void setBindSearchParameterNames (List<String> bindSearchParameterNames)
 {
  this.bindSearchParameterNames = bindSearchParameterNames;
 }

 /**
  * Get bindSearchParameterNames
  *
  * optional
  *
  * The corresponding names to the search criteria parameters supplying a user
  * entered value to the search
  */
 @Override
 public List<String> getBindSearchParameterNames ()
 {
  return this.bindSearchParameterNames;
 }
 private List<String> bindFieldKeys;

 /**
  * Set bindFieldKeys
  *
  * optional
  *
  * The key to one or more fields defined in the search criteria section
  */
 @Override
 public void setBindFieldKeys (List<String> bindFieldKeys)
 {
  this.bindFieldKeys = bindFieldKeys;
 }

 /**
  * Get bindFieldKeys
  *
  * optional
  *
  * The key to one or more fields defined in the search criteria section
  */
 @Override
 public List<String> getBindFieldKeys ()
 {
  return this.bindFieldKeys;
 }
 private List<String> resultColumnKeys;

 /**
  * Set resultColumnKeys
  *
  * optional
  *
  * The set of keys to the columns in the tabular data to be returned, must be a
  * subset (proper ok) and the values exactly match the result column keys for the
  * search type as named in the search
  * specification.xls.
  * If not specified ALL result columns as defined in the search specification are
  * returned from the search
  */
 @Override
 public void setResultColumnKeys (List<String> resultColumnKeys)
 {
  this.resultColumnKeys = resultColumnKeys;
 }

 /**
  * Get resultColumnKeys
  *
  * optional
  *
  * The set of keys to the columns in the tabular data to be returned, must be a
  * subset (proper ok) and the values exactly match the result column keys for the
  * search type as named in the search
  * specification.xls.
  * If not specified ALL result columns as defined in the search specification are
  * returned from the search
  */
 @Override
 public List<String> getResultColumnKeys ()
 {
  return this.resultColumnKeys;
 }
 private List<String> resultColumnHeaders;

 /**
  * Set resultColumnHeaders
  *
  * optional
  *
  * Values to use as column headers, may be used to override the default search
  * result column
  * names.
  * If not specified then the default column headers defined in the search are used
  * as the column headers
  */
 @Override
 public void setResultColumnHeaders (List<String> resultColumnHeaders)
 {
  this.resultColumnHeaders = resultColumnHeaders;
 }

 /**
  * Get resultColumnHeaders
  *
  * optional
  *
  * Values to use as column headers, may be used to override the default search
  * result column
  * names.
  * If not specified then the default column headers defined in the search are used
  * as the column headers
  */
 @Override
 public List<String> getResultColumnHeaders ()
 {
  return this.resultColumnHeaders;
 }
 private List<String> hiddenColumnKeys;

 /**
  * Set hiddenColumnKeys
  *
  * optional
  *
  * Identifies the columns which by default are
  * hidden.
  * If not specified then all columns are visible
  */
 @Override
 public void setHiddenColumnKeys (List<String> hiddenColumnKeys)
 {
  this.hiddenColumnKeys = hiddenColumnKeys;
 }

 /**
  * Get hiddenColumnKeys
  *
  * optional
  *
  * Identifies the columns which by default are
  * hidden.
  * If not specified then all columns are visible
  */
 @Override
 public List<String> getHiddenColumnKeys ()
 {
  return this.hiddenColumnKeys;
 }
 private SearchGwtServiceAsync searchServicce;

 public void setSearchService (SearchGwtServiceAsync searchServicce)
 {
  this.searchServicce = searchServicce;
 }

 public SearchGwtServiceAsync getSearchService ()
 {
  return this.searchServicce;
 }
}

