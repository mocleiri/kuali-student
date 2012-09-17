package com.sigmasys.kuali.ksa.krad.util;

import com.sigmasys.kuali.ksa.model.SearchTypeValue;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: dmulderink on 9/11/12 at 12:28 PM
 */
public class SearchTypeKeyValues extends KeyValuesBase {

   private boolean blankOption;

   /**
    * This is an implementation of a key value finder, normally this would make a request to
    * a database to obtain the necessary values.
    *
    * @see org.kuali.rice.krad.keyvalues.KeyValuesFinder#getKeyValues()
    */
   @Override
   public List<KeyValue> getKeyValues() {
      List<KeyValue> keyValues = new ArrayList<KeyValue>();
      if(blankOption){
         keyValues.add(new ConcreteKeyValue("", ""));
      }
      keyValues.add(new ConcreteKeyValue(SearchTypeValue.ACCOUNTS_CODE, SearchTypeValue.ACCOUNTS.toString()));
      keyValues.add(new ConcreteKeyValue(SearchTypeValue.ALERT_FLAG_CODE, SearchTypeValue.ALERT_FLAG.toString()));
      keyValues.add(new ConcreteKeyValue(SearchTypeValue.MEMO_CODE, SearchTypeValue.MEMO.toString()));

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
