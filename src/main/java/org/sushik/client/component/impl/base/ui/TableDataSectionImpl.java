/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sushik.client.component.infc.base.ui.ContextToolbar;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.SearchCriteriaSection;
import org.sushik.client.component.infc.base.ui.TabSectionHeader;
import org.sushik.client.component.infc.base.ui.TableDataSection;
import org.sushik.client.component.infc.base.ui.TableDataSource;
import org.sushik.client.component.infc.base.ui.TableValueLinkBinding;
import org.sushik.client.search.api.Result;
import org.sushik.client.search.api.ResultCell;
import org.sushik.client.search.api.ResultColumnInfo;
import org.sushik.client.search.api.SearchTypeInfo;

/**
 * A section that displays a table of data with optional links to allow the user to
 * choose a row and navigate to another page
 *
 * Configured as follows:
 *
 * headerStyle - Style to use for the header
 *     section.header
 *
 * currentTabStyle - The current tab
 *     tab.section.header
 *
 */
public class TableDataSectionImpl extends SectionImpl
  implements TableDataSection
{

 public TableDataSectionImpl ()
 {
  super ();
 }
 private VerticalPanel panel;

 @Override
 public Object getImpl ()
 {
  return panel;
 }

 @Override
 public void hide ()
 {
  this.panel.setVisible (false);
 }
 private Grid table = null;

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  this.panel = new VerticalPanel ();
  if (this.getHeaderText () != null)
  {
   Label label = new Label ();
   label.setText (this.getHeaderText ());
   this.panel.add (label);
  }
// tab panel if any
  if (this.getCurrentTabText () != null)
  {
   HorizontalPanel tabs = new HorizontalPanel ();
   panel.add (tabs);

   if (this.getLeftTabs () != null)
   {
    for (TabSectionHeader tab : this.getLeftTabs ())
    {
     tab.init (this);
     tabs.add ((Widget) tab.getImpl ());
    }
   }
   Button currentTab = new Button ();
   currentTab.setText (this.getCurrentTabText ());
   tabs.add (currentTab);
   if (this.getRightTabs () != null)
   {
    for (TabSectionHeader tab : this.getRightTabs ())
    {
     tab.init (this);
     tabs.add ((Widget) tab.getImpl ());
    }
   }
  }

  if (this.getCriteria () != null)
  {
   this.getCriteria ().init (this);
   this.panel.add ((Widget) this.getCriteria ().getImpl ());
  }

  if (this.getToolbar () != null)
  {
   this.getToolbar ().init (this);
   this.panel.add ((Widget) this.getToolbar ().getImpl ());
  }

  if (this.getTableDataSource () != null)
  {
   this.getTableDataSource ().init (this);
  }

  table = new Grid ();
  // get the header data for the table
  log ("init: getting search type info");
  this.getTableDataSource ().getSearchTypeInfo (
    new SearchTypeInfoCallback (this));
  this.panel.add (table);

 }

 private void log (String message)
 {
//  log (" " + this.getClass ().getName () + ": " + message);
 }

 @Override
 public void show ()
 {
  this.panel.setVisible (true);
 }
 private SearchTypeInfo searchTypeInfo = null;

 public SearchTypeInfo getSearchTypeInfo ()
 {
  return searchTypeInfo;
 }

 public void setSearchTypeInfo (SearchTypeInfo searchTypeInfo)
 {
  this.searchTypeInfo = searchTypeInfo;
 }

 private class SearchTypeInfoCallback
   implements AsyncCallback<SearchTypeInfo>
 {

  private TableDataSectionImpl tds;

  public SearchTypeInfoCallback (TableDataSectionImpl tds)
  {
   this.tds = tds;
  }

  @Override
  public void onFailure (Throwable caught)
  {
   log ("SearchTypeInfoCallback got error caught =" + caught.getClass ().getName ());
   log ("SearchTypeInfoCallback got error caught =" + caught.getMessage ());
   log ("SearchTypeInfoCallback got error caught =" + caught.getCause ());
//   DisplayErrorPage errorPage = new DisplayErrorPageImpl ();
//   errorPage.init (tds);
//   errorPage.setMessageText (caught.getLocalizedMessage ());
//   errorPage.show ();
   Window.alert (caught.getMessage ());
  }

  public void onSuccess (SearchTypeInfo result)
  {
   log ("SearchTypeInfoCallback success");
   tds.setSearchTypeInfo (result);
   tds.buildTableAfterGotData ();
  }
 }
 private List<Result> results = null;

 public List<Result> getResults ()
 {
  return results;
 }

 public void setResults (List<Result> results)
 {
  this.results = results;
 }

 private class ResultsCallback
   implements AsyncCallback<List<Result>>
 {

  private TableDataSectionImpl tds;

  public ResultsCallback (TableDataSectionImpl tds)
  {
   this.tds = tds;
  }

  @Override
  public void onFailure (Throwable caught)
  {
//   DisplayErrorPage errorPage = new DisplayErrorPageImpl ();
//   errorPage.init (tds);
//   errorPage.setMessageText (caught.getLocalizedMessage ());
//   errorPage.show ();
   Window.alert (caught.getMessage ());
  }

  public void onSuccess (List<Result> result)
  {
   tds.setResults (result);
   tds.buildTableAfterGotData ();
  }
 }

 private void buildTableAfterGotData ()
 {
  log ("buildTableAfterGotData - entered");
  if (this.getSearchTypeInfo () == null)
  {
   log ("buildTableAfterGotData no search type info so leaving");
   return;
  }
  int rows = 1;
  if (this.getResults () != null)
  {
   rows = this.getResults ().size () + 1;
  }
  table.resize (rows, this.getSearchTypeInfo ().getSearchResultTypeInfo ().
    getResultColumns ().size ());
  this.buildTableHeader ();
  if (this.getResults () != null)
  {
   this.buildTableBody ();
  }
  this.show ();
  return;
 }

 private void buildTableHeader ()
 {
  List<ResultColumnInfo> list = this.getSearchTypeInfo ().
    getSearchResultTypeInfo ().getResultColumns ();
  for (int i = 0; i < list.size (); i ++)
  {
   ResultColumnInfo info = list.get (i);
   table.setText (0, i, info.getFieldDescriptor ().getName ());
  }
 }

 private void buildTableBody ()
 {
  int row = 1;
  for (Result result : results)
  {
   for (int i = 0; i < result.getResultCells ().size (); i ++)
   {
    ResultCell cell = result.getResultCells ().get (i);
    table.setText (row, i, cell.getValue ());
   }
   row ++;
  }
 }

 @Override
 public void displayTableData ()
 {
  // clear the results
  log ("Displaying table data");
  results = null;
  searchTypeInfo = null;
  this.getTableDataSource ().getSearchTypeInfo (
    new SearchTypeInfoCallback (this));
  this.getTableDataSource ().getResults (new ResultsCallback (this));
 }


	private String headerText;

	/**
	* Set headerText
	*
	* optional
	*
	* The header to display at the top of this section
	*/
	@Override
	public void setHeaderText (String headerText)
	{
		this.headerText = headerText;
	}

	/**
	* Get headerText
	*
	* optional
	*
	* The header to display at the top of this section
	*/
	@Override
	public String getHeaderText ()
	{
		return this.headerText;
	}


	private String headerStyle;

	/**
	* Set headerStyle
	*
	* optional
	*
	* Default Value:
	* section-header
	*
	* Style to use for the header
	*/
	@Override
	public void setHeaderStyle (String headerStyle)
	{
		this.headerStyle = headerStyle;
	}

	/**
	* Get headerStyle
	*
	* optional
	*
	* Style to use for the header
	*/
	@Override
	public String getHeaderStyle ()
	{
		return this.headerStyle;
	}


	private List<TabSectionHeader> leftTabs;

	/**
	* Set leftTabs
	*
	* optional
	*
	* Tabs that appear to the left of the current tab
	*/
	@Override
	public void setLeftTabs (List<TabSectionHeader> leftTabs)
	{
		this.leftTabs = leftTabs;
	}

	/**
	* Get leftTabs
	*
	* optional
	*
	* Tabs that appear to the left of the current tab
	*/
	@Override
	public List<TabSectionHeader> getLeftTabs ()
	{
		return this.leftTabs;
	}


	private String currentTabText;

	/**
	* Set currentTabText
	*
	* optional
	*
	* The current tab
	*/
	@Override
	public void setCurrentTabText (String currentTabText)
	{
		this.currentTabText = currentTabText;
	}

	/**
	* Get currentTabText
	*
	* optional
	*
	* The current tab
	*/
	@Override
	public String getCurrentTabText ()
	{
		return this.currentTabText;
	}


	private String currentTabStyle;

	/**
	* Set currentTabStyle
	*
	* optional
	*
	* Default Value:
	* tab.section.header
	*
	* The current tab
	*/
	@Override
	public void setCurrentTabStyle (String currentTabStyle)
	{
		this.currentTabStyle = currentTabStyle;
	}

	/**
	* Get currentTabStyle
	*
	* optional
	*
	* The current tab
	*/
	@Override
	public String getCurrentTabStyle ()
	{
		return this.currentTabStyle;
	}


	private List<TabSectionHeader> rightTabs;

	/**
	* Set rightTabs
	*
	* optional
	*
	* Tabs that appear to the right of the current tab
	*/
	@Override
	public void setRightTabs (List<TabSectionHeader> rightTabs)
	{
		this.rightTabs = rightTabs;
	}

	/**
	* Get rightTabs
	*
	* optional
	*
	* Tabs that appear to the right of the current tab
	*/
	@Override
	public List<TabSectionHeader> getRightTabs ()
	{
		return this.rightTabs;
	}


	private SearchCriteriaSection criteria;

	/**
	* Set criteria
	*
	* required
	*
	* The critiera used to generate the table of data
	*/
	@Override
	public void setCriteria (SearchCriteriaSection criteria)
	{
		this.criteria = criteria;
	}

	/**
	* Get criteria
	*
	* required
	*
	* The critiera used to generate the table of data
	*/
	@Override
	public SearchCriteriaSection getCriteria ()
	{
		return this.criteria;
	}


	private ContextToolbar toolbar;

	/**
	* Set toolbar
	*
	* optional
	*
	* Bar of buttons for user to navigate
	*/
	@Override
	public void setToolbar (ContextToolbar toolbar)
	{
		this.toolbar = toolbar;
	}

	/**
	* Get toolbar
	*
	* optional
	*
	* Bar of buttons for user to navigate
	*/
	@Override
	public ContextToolbar getToolbar ()
	{
		return this.toolbar;
	}


	private TableDataSource tableDataSource;

	/**
	* Set tableDataSource
	*
	* required
	*
	* The source of the tabular data to be displayed
	*/
	@Override
	public void setTableDataSource (TableDataSource tableDataSource)
	{
		this.tableDataSource = tableDataSource;
	}

	/**
	* Get tableDataSource
	*
	* required
	*
	* The source of the tabular data to be displayed
	*/
	@Override
	public TableDataSource getTableDataSource ()
	{
		return this.tableDataSource;
	}


	private List<TableValueLinkBinding> linkBindings;

	/**
	* Set linkBindings
	*
	* optional
	*
	* The bindings to values in columns to link to new pages
	*/
	@Override
	public void setLinkBindings (List<TableValueLinkBinding> linkBindings)
	{
		this.linkBindings = linkBindings;
	}

	/**
	* Get linkBindings
	*
	* optional
	*
	* The bindings to values in columns to link to new pages
	*/
	@Override
	public List<TableValueLinkBinding> getLinkBindings ()
	{
		return this.linkBindings;
	}

}

