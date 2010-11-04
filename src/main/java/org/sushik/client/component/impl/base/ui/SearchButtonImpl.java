/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.SearchButton;
import org.sushik.client.component.infc.base.ui.TableDataSection;

/**
 * Button that executes the search
 *
 * Configured as follows:
 *
 * text - The label on the button
 *     Search
 *
 * style - Name of the page to which this link routes to
 *     toolbar.button
 *
 * helpText - Help text to display perhaps as a hover for the link
 *     Execute the search and display any results
 *
 * helpStyle - Style to use when displaying the help text
 *     toolbar.button.help
 *
 * nextPage - Name of the page to which this link routes to
 *     <currentPage>
 *
 * parameterNames - unique name of the parameter
 *     <parameterNames>
 *
 * parameterValues - Corresponding value of the parameter
 *     <parameterValues>
 *
 */
public class SearchButtonImpl extends ToolbarButtonImpl
    implements SearchButton
{

 public SearchButtonImpl ()
 {
  super ();
 }

 private TableDataSection findParentTableDataSection (NestedComponent child)
 {
  if (child instanceof TableDataSection)
  {
   return (TableDataSection) child;
  }
  return findParentTableDataSection (child.getParent ());
 }

}

