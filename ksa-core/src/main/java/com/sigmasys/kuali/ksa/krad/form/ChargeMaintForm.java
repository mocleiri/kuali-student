package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Charge;
import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * User: dmulderink
 * Date: 4/8/12
 * Time: 7:58 PM
 */

/**
 * This is the Charge maintenance form view
 * Call via an actionLink in the AlertsTransactionView
 */
public class ChargeMaintForm extends UifFormBase {

   private Charge charge;

   /**
    * Get the Charge model
    * @return
    */
   public Charge getCharge() {
      return charge;
   }

   /**
    * Set the charge model
    * @param charge
    */
   public void setCharge(Charge charge) {
      this.charge = charge;
   }
}
