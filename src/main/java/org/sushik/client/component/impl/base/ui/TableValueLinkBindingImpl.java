/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Hyperlink;
import java.util.List;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.TableValueLinkBinding;


/**
 * A navigation item that brings the user back to a previous point in the
 * navigation trail
 *
 * Configured as follows:
 *
 * style - style to apply
 *     table.value.link
 *
 * helpStyle - Style to use when displaying the help text
 *     table.value.link.help
 *
 */
public class TableValueLinkBindingImpl extends NextPageLinkImpl
  implements TableValueLinkBinding
{

 public TableValueLinkBindingImpl ()
 {
  super ();
 }


 @Override
 public void click ()
 {
  // TODO: replace null with a clickEven
  this.gwtClickHandler.onClick (null);
 }
 private Hyperlink link;

 @Override
 public Object getImpl ()
 {
  return link;
 }

 @Override
 public void hide ()
 {
  link.setVisible (false);
 }
 
 private HandlerRegistration handlerRegistration;
 private GwtClickHandler gwtClickHandler;

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  this.link = new Hyperlink ();
  link.setText (this.getText ());
  link.setTargetHistoryToken (this.getText ());
  gwtClickHandler = new GwtClickHandler (this);
  handlerRegistration = link.addClickHandler (gwtClickHandler);
 }

 @Override
 public void show ()
 {
  link.setVisible (true);
 }

 private boolean enabled = false;

 @Override
 public boolean isEnabled ()
 {
  return enabled;
 }

 @Override
 public void setEnabled (boolean enabled)
 {
  this.enabled = enabled;
  // TODO: change the style to it is dimmed if not enabled
  return;
 }


	private Boolean useValueAsText;

	/**
	* Set useValueAsText
	*
	* required
	*
	* Indicates the value of the table cell should be used as the text
	*/
	@Override
	public void setUseValueAsText (Boolean useValueAsText)
	{
		this.useValueAsText = useValueAsText;
	}

	/**
	* Get useValueAsText
	*
	* required
	*
	* Indicates the value of the table cell should be used as the text
	*/
	@Override
	public Boolean isUseValueAsText ()
	{
		return this.useValueAsText;
	}


	private List<String> bindParameterNames;

	/**
	* Set bindParameterNames
	*
	* optional
	*
	* The name of the parameter that is bound to the column cell's value
	*/
	@Override
	public void setBindParameterNames (List<String> bindParameterNames)
	{
		this.bindParameterNames = bindParameterNames;
	}

	/**
	* Get bindParameterNames
	*
	* optional
	*
	* The name of the parameter that is bound to the column cell's value
	*/
	@Override
	public List<String> getBindParameterNames ()
	{
		return this.bindParameterNames;
	}


	private List<String> bindResultColumnKeys;

	/**
	* Set bindResultColumnKeys
	*
	* optional
	*
	* The key to the table colum the value to which is bound to the parameter
	*/
	@Override
	public void setBindResultColumnKeys (List<String> bindResultColumnKeys)
	{
		this.bindResultColumnKeys = bindResultColumnKeys;
	}

	/**
	* Get bindResultColumnKeys
	*
	* optional
	*
	* The key to the table colum the value to which is bound to the parameter
	*/
	@Override
	public List<String> getBindResultColumnKeys ()
	{
		return this.bindResultColumnKeys;
	}
	
}

