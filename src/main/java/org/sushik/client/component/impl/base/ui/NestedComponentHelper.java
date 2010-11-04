/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import java.util.ArrayList;
import java.util.Date;
import org.sushik.client.component.infc.base.ComponentConfigurationLogic;
import org.sushik.client.component.infc.base.ui.NestedComponent;

/**
 *
 * @author nwright
 */
public class NestedComponentHelper
{

 private NestedComponent component;

 public NestedComponentHelper (NestedComponent component)
 {
  this.component = component;
 }

 public void init (NestedComponent parent)
 {
  component.setParent (parent);
  if (parent != null)
  {
   if (parent.getChildren () == null)
   {
    parent.setChildren (new ArrayList ());
   }
   // children
   parent.getChildren ().add (component);
  }

  // dispatch commands to configure the component once all settles down
  if (this.component.getDynamicConfiguration () != null)
  {
   for (ComponentConfigurationLogic logic :
        this.component.getDynamicConfiguration ())
   {
    log ("adding logic commands for deferred execution " + logic);
    Command command = new LogicCommand (logic, this.component);
    DeferredCommand.addCommand (command);
   }
  }
 }

 private void log (String message)
 {
  System.out.println (new Date () + " " + this.getClass ().getName () + ": "
                      + message);
 }

 private static class LogicCommand implements Command
 {

  private ComponentConfigurationLogic logic;
  private NestedComponent comp;

  public LogicCommand (ComponentConfigurationLogic logic,
                       NestedComponent comp)
  {
   this.logic = logic;
   this.comp = comp;
  }

  @Override
  public void execute ()
  {
   log ("about to execture logic " + logic + " on comp " + comp);
   logic.configure (comp);
  }

  private void log (String message)
  {
   System.out.println (new Date () + " " + this.getClass ().getName () + ": "
                       + message);
  }
 }
}
