/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Hyperlink;
import java.util.List;
import org.sushik.client.component.infc.base.ui.Link;
import org.sushik.client.component.infc.base.ui.NestedComponent;
import org.sushik.client.component.infc.factory.ComponentEnum;


/**
 * A link used for navigation
 *
 * Configured as follows:
 *
 * style - Name of the page to which this link routes to
 *     link
 *
 * helpStyle - Style to use when displaying the help text
 *     link.help
 *
 */
public class LinkImpl extends UserInteractableUIComponentImpl
  implements Link
{

 public LinkImpl ()
 {
  super ();
 }

 private String helpStyle;

 @Override
 public String getHelpStyle ()
 {
  return this.helpStyle;
 }

 private String helpText;

 @Override
 public String getHelpText ()
 {
 return this.helpText;
 }

 private ComponentEnum nextPage;
 @Override
 public ComponentEnum getNextPage ()
 {
  return nextPage;
 }

 private List<String> parameterNames;

 @Override
 public List<String> getParameterNames ()
 {
  return this.parameterNames;
 }

 private List<String> parameterValues;
 @Override
 public List<String> getParameterValues ()
 {
  return this.parameterValues;
 }

 private String style;

 @Override
 public String getStyle ()
 {
 return this.style;
 }

 private String text;
 @Override
 public String getText ()
 {
  return this.text;
 }

 @Override
 public void setHelpStyle (String helpStyle)
 {
  this.helpStyle = helpStyle;
 }

 @Override
 public void setHelpText (String helpText)
 {
  this.helpText = helpText;
 }

 @Override
 public void setNextPage (ComponentEnum nextPage)
 {
  this.nextPage = nextPage;
 }

 @Override
 public void setParameterNames (List<String> parameterNames)
 {
  this.parameterNames = parameterNames;
 }

 @Override
 public void setParameterValues (List<String> parameterValues)
 {
  this.parameterValues = parameterValues;
 }

 @Override
 public void setStyle (String style)
 {
  this.style = style;
 }

 @Override
 public void setText (String text)
 {
  this.text = text;
 }



 @Override
 public void click ()
 {
  ClickEvent clickEvent = new GwtProgramaticClickEvent ();
  this.gwtClickHandler.onClick (clickEvent);
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
  if (this.enabled)
  {
   this.link.setTitle (this.getText () + " enabled");
  }
  else
  {
   this.link.setTitle (this.getText () + " disabled");
  }
  return;
 }

}

