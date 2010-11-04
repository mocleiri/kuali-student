/*
 * Copywright Wright Communications, Inc.
 *
 * All rights Reserved
 */
package org.sushik.client.component.impl.base.ui;

import java.util.Date;
import java.util.List;
import org.sushik.client.component.impl.base.ComponentConfigurationLogicImpl;
import org.sushik.client.component.infc.base.Component;
import org.sushik.client.component.infc.base.ui.EnableComponentConfigurationLogic;
import org.sushik.client.component.infc.base.ui.UserInteractableUIComponent;

/**
 *
 * @author nwright
 */
public class EnableComponentConfigurationLogicImpl extends ComponentConfigurationLogicImpl
  implements EnableComponentConfigurationLogic
{

 public EnableComponentConfigurationLogicImpl ()
 {
  super ();
 }

 private void log (String message)
 {
  System.out.println (new Date () + " " + this.getClass ().getName () + ": "
                      + message);
 }
 private List<String> atLeasetOneOfTheseRoles;

 @Override
 public List<String> getAtLeastOneOfTheseRoles ()
 {
  return this.atLeasetOneOfTheseRoles;
 }
 private boolean requireAuthentication;

 @Override
 public Boolean isRequireAuthentication ()
 {
  return this.requireAuthentication;
 }

 @Override
 public void setAtLeastOneOfTheseRoles (List<String> atLeastOneOfTheseRoles)
 {
  this.atLeasetOneOfTheseRoles = atLeastOneOfTheseRoles;
 }

 @Override
 public void setRequireAuthentication (Boolean requireAuthentication)
 {
  this.requireAuthentication = requireAuthentication;
 }

 @Override
 public void configure (Component comp)
 {
  UserInteractableUIComponent uicomp = (UserInteractableUIComponent) comp;
  if ( ! this.isRequireAuthentication ())
  {
   uicomp.setEnabled (true);
   return;
  }
//  TODO: Get the current user to see if authenticated
//  TODO: Check if user has at least one of those roles
 }

// private void handleEvent ()
// {
//
//  if (event.getResult () == null)
//  {
//   log ("Disabling " + uicomp + " because not authenticated");
//   uicomp.setEnabled (false);
//   return;
//  }
//
//  // we have an authenticated user so now check if roles need to be checked
//  if (this.getAtLeastOneOfTheseRoles () == null)
//  {
//   uicomp.setEnabled (true);
//   return;
//  }
//  if (this.getAtLeastOneOfTheseRoles ().size () == 0)
//  {
//   uicomp.setEnabled (true);
//   return;
//  }
//  throw new IllegalArgumentException (
//    "roles checking is not supported at this time");
// }
}
