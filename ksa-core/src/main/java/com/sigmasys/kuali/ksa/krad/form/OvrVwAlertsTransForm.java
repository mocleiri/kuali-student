package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.temp.AccountTrans;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class OvrVwAlertsTransForm extends UifFormBase {
   private static final long serialVersionUID = -7525378097732916418L;

   private Transaction alertTransaction;

   private List<AccountTrans> accntTransLst= new ArrayList<AccountTrans>();

   private BigDecimal totalAmnt = new BigDecimal(BigInteger.ZERO);

   public Transaction getTransaction() {
      return alertTransaction;
   }

   public void setTransaction(Transaction transaction) {
      this.alertTransaction = transaction;
   }

   public BigDecimal getFormattedAmount() {
      if ( alertTransaction != null ) {
         BigDecimal amount = alertTransaction.getAmount();
         if (amount != null) {
            return amount.setScale(5, BigDecimal.ROUND_CEILING);
         }
      }
      return BigDecimal.ZERO;
   }


   public List<AccountTrans> getAccntTransLst() {
      return accntTransLst;
   }

   public void setAccntTransLst(List<AccountTrans> accntTransLst) {
      this.accntTransLst = accntTransLst;
   }


   public BigDecimal getTotalAmnt() {
      if (totalAmnt != null) {
         return totalAmnt.setScale(5, BigDecimal.ROUND_CEILING);
      }

      return BigDecimal.ZERO;
   }

   public void setTotalAmnt(BigDecimal totalAmnt) {
      this.totalAmnt = totalAmnt;
   }
}
