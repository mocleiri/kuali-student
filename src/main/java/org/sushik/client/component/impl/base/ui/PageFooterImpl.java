/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.base.ui.PageFooter;


/**
 * A band at the bottom of the page
 *
 * Configured as follows:
 *
 * style - Style for the footer label
 *     page.footer
 *
 */
public class PageFooterImpl extends BandImpl
    implements PageFooter
{

 public PageFooterImpl ()
 {
  super ();
 }

 private DockPanel panel;

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

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  this.panel = new DockPanel ();
  this.panel.setWidth ("100%");
  if (this.getText () != null)
  {
   Label label = new Label ();
   label.setText (this.getText ());
   panel.add (label, DockPanel.CENTER);
  }
 }

 @Override
 public void show ()
 {
  this.panel.setVisible (true);
 }

 	private String text;

	/**
	* Set text
	*
	* required
	*
	* text of the footer
	*/
	@Override
	public void setText (String text)
	{
		this.text = text;
	}

	/**
	* Get text
	*
	* required
	*
	* text of the footer
	*/
	@Override
	public String getText ()
	{
		return this.text;
	}


	private String style;

	/**
	* Set style
	*
	* required
	*
	* Default Value:
	* page-footer
	*
	* Style for the footer label
	*/
	@Override
	public void setStyle (String style)
	{
		this.style = style;
	}

	/**
	* Get style
	*
	* required
	*
	* Style for the footer label
	*/
	@Override
	public String getStyle ()
	{
		return this.style;
	}
}

