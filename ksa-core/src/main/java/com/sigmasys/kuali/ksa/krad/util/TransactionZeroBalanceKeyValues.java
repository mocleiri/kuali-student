package com.sigmasys.kuali.ksa.krad.util;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * User: dmulderink
 * Date: 4/21/12
 * Time: 2:02 PM
 */
public class TransactionZeroBalanceKeyValues extends KeyValuesBase {

   private boolean blankOption;

    private List<Date> zeroBalance;

   @Override
   public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        if(blankOption){
            keyValues.add(new ConcreteKeyValue("", ""));
        }

       if(this.zeroBalance == null){
           return keyValues;
       }

       DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

       for(Date zero : zeroBalance) {
           String zeroString = df.format(zero);
           keyValues.add(new ConcreteKeyValue(zeroString, "Zero Balance on " + zeroString));
       }


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

    public void setValues(List<Date> zeroBalanceDates) {
        this.zeroBalance = zeroBalanceDates;
    }
}
