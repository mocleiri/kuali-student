/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base;

import java.util.List;
import org.sushik.client.component.infc.base.Component;
import org.sushik.client.component.infc.base.ComponentConfigurationLogic;
import org.sushik.client.component.infc.factory.ComponentEnum;




/**
* component implementation
*/
public class ComponentImpl
 implements Component
{
	
	public ComponentImpl ()
	{
		super ();
	}
 private ComponentEnum componentEnum;
 private String componentId;
 private List<ComponentConfigurationLogic> dynamicConfiguration;

 @Override
 public ComponentEnum getComponentEnum ()
 {
 return this.componentEnum;
 }

 @Override
 public String getComponentId ()
 {
  return this.componentId;
 }

 @Override
 public List<ComponentConfigurationLogic> getDynamicConfiguration ()
 {
  return this.dynamicConfiguration;
 }

 @Override
 public void setComponentEnum (ComponentEnum componentEnum)
 {
  this.componentEnum = componentEnum;
 }

 @Override
 public void setComponentId (String componentId)
 {
  this.componentId = componentId;
 }

 @Override
 public void setDynamicConfiguration (List<ComponentConfigurationLogic> dynamicConfiguration)
 {
  this.dynamicConfiguration = dynamicConfiguration;
 }

}

