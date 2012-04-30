package com.sigmasys.kuali.ksa.krad.util;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dmulderink
 * Date: 4/21/12
 * Time: 2:01 PM
 */
public class PaymentTypeKeyValues extends KeyValuesBase {

   private boolean blankOption;

   /**
    * This is a fake implementation of a key value finder, normally this would make a request to
    * a database to obtain the necessary values.  Used only for testing.
    *
    * @see org.kuali.rice.krad.keyvalues.KeyValuesFinder#getKeyValues()
    */
   @Override
   public List<KeyValue> getKeyValues() {
      List<KeyValue> keyValues = new ArrayList<KeyValue>();
      if(blankOption){
         keyValues.add(new ConcreteKeyValue("", ""));
      }
      keyValues.add(new ConcreteKeyValue("0", "Cash"));                       // cash
      keyValues.add(new ConcreteKeyValue("1", "Check - In Person"));          // chip
      keyValues.add(new ConcreteKeyValue("2", "Check - Mail"));               // chma
      keyValues.add(new ConcreteKeyValue("3", "Credit card - Email"));        // ccma
      keyValues.add(new ConcreteKeyValue("4", "Credit card In Person"));      // ccip
      keyValues.add(new ConcreteKeyValue("5", "Credit card On Line"));        // ccol
      keyValues.add(new ConcreteKeyValue("6", "Electronic funds transfer"));  // ach

      return keyValues;
   }

   /**
    * @return the blankOption
    */
   public boolean isBlankOption() {
      return this.blankOption;
   }

   /**
    * @param blankOption the blankOption to set
    */
   public void setBlankOption(boolean blankOption) {
      this.blankOption = blankOption;
   }
}
