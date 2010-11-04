/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import java.util.List;
import org.sushik.client.component.impl.base.ComponentImpl;
import org.sushik.client.component.infc.base.ui.NestedComponent;

/**
 * Logic that computes the user should be routed to next
 *
 * Configured as follows:
 *
 */
public class NestedComponentImpl extends ComponentImpl
  implements NestedComponent
{

 public NestedComponentImpl ()
 {
  super ();
 }

 @Override
 public void init (NestedComponent parent)
 {
  new NestedComponentHelper (this).init (parent);
 }
 private List<NestedComponent> children;

 @Override
 public List<NestedComponent> getChildren ()
 {
  return children;
 }
 private NestedComponent parent;

 @Override
 public NestedComponent getParent ()
 {
  return this.parent;
 }

 @Override
 public void setChildren (List<NestedComponent> children)
 {
  this.children = children;
 }

 @Override
 public void setParent (NestedComponent parent)
 {
  this.parent = parent;
 }
}

