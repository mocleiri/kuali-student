package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Activity;

import java.util.List;

/**
 * Created by: dmulderink on 10/5/12 at 6:55 PM
 */
public class AdminForm extends AbstractViewModel {

   private static final long serialVersionUID = -7525378097732916418L;

   /*
     Activities
   */

   // a list of activities
   private List<Activity> activities;

   /*
     Get / Set methods
   */

   public List<Activity> getActivities() {
      return activities;
   }

   public void setActivities(List<Activity> activities) {
      this.activities = activities;
   }
}
