/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sushik.client.component.infc.base.ui.MainMenuButton;
import org.sushik.client.component.infc.base.ui.MainMenuSection;
import org.sushik.client.component.infc.base.ui.NestedComponent;



/**
* A section that presents a menu of options from the user to choose
*
* Configured as follows:
*
* style - Style to use for the header
*     main.menu.header
*
*/
public class MainMenuSectionImpl extends SectionImpl
 implements MainMenuSection
{
	
	public MainMenuSectionImpl ()
	{
		super ();
	}

 private VerticalPanel section;

 @Override
 public Object getImpl ()
 {
  return section;
 }

 
 @Override
 public void hide ()
 {
  this.section.setVisible (false);
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
  this.section = new VerticalPanel ();
  Label label = new Label ();
  label.setText (this.getText ());
  this.section.add (label);
  for (MainMenuButton button : this.getButtons ())
  {
   button.init (this);
   this.section.add ((Widget) button.getImpl ());
  }
 }

 @Override
 public void show ()
 {
  this.section.setVisible (true);
 }


	private String text;

	/**
	* Set text
	*
	* optional
	*
	* The header to display at the top of this section
	*/
	@Override
	public void setText (String text)
	{
		this.text = text;
	}

	/**
	* Get text
	*
	* optional
	*
	* The header to display at the top of this section
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
	* main-menu-header
	*
	* Style to use for the header
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
	* Style to use for the header
	*/
	@Override
	public String getStyle ()
	{
		return this.style;
	}


	private List<MainMenuButton> buttons;

	/**
	* Set buttons
	*
	* optional
	*
	* The source of the tabular data to be displayed
	*/
	@Override
	public void setButtons (List<MainMenuButton> buttons)
	{
		this.buttons = buttons;
	}

	/**
	* Get buttons
	*
	* optional
	*
	* The source of the tabular data to be displayed
	*/
	@Override
	public List<MainMenuButton> getButtons ()
	{
		return this.buttons;
	}
	
}

