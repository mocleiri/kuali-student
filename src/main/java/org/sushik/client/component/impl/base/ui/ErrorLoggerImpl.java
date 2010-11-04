/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base.ui;

import com.google.gwt.core.client.GWT;
import org.sushik.client.component.impl.base.ComponentImpl;
import org.sushik.client.component.infc.common.ui.ErrorLogger;

/**
 *
 * @author nwright
 */
public class ErrorLoggerImpl extends ComponentImpl
  implements ErrorLogger
{

 @Override
 public void error (Object msg)
 {
  GWT.log (msg.toString ());
 }

 @Override
 public void error (Object msg, Throwable t)
 {
  GWT.log (msg.toString (), t);
 }

}
