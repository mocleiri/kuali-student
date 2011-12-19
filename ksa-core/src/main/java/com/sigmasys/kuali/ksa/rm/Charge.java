package com.sigmasys.kuali.ksa.rm;

public class Charge extends Debit {

    /* If a payment is deferred, this flag will be set to true.
          * 	 * In addition, the attribute defermentId will be set to the identifier of the deferment. The
          * 	 	 */
    private Boolean isDeferred;
    /*
              * 	 * The defermentId points towards the deferment that has been allocated to the charge. While a charge is deferred, it is considered
              * 	 	 * 'paid' until the deferment expires.
              * 	 	 	 */
    private String defermentId;


    /*
                  * 	 * Clearing the deferments status will deallocate the payment, clear the defermentId and the isDeferred flag. This method will be called
                  * 	 	 * through the expire() method of the Deferment class.
                  * 	 	 	 */
    public void clearDefermentStatus() {

    }


    /*
                      * 	 * Calling defer will set both the isDeferred flag and will set the defermentId. It will be called during the creation of a Deferment
                      * 	 	 * instance.
                      * 	 	 	 */

    public void defer(String defermentId) {

    }


}

