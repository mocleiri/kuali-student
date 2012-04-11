package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Charge;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

public class AlertsTransactionForm extends UifFormBase {

   private List<Charge> charges;

   private BigDecimal totalAmnt = new BigDecimal(BigInteger.ZERO);

   private Date lastLedgerDate;

   private String lastStatementText;

   private String workSetRows;

   private Date minQueryDate;

   private Date maxQueryDate;

   /**
    * Get the total amount calculated from all list charges
    * @return
    */
   public BigDecimal getTotalAmnt() {
      if (totalAmnt != null) {
         return totalAmnt.setScale(5, BigDecimal.ROUND_CEILING);
      }

      return BigDecimal.ZERO;
   }

   /**
    * Set the total amount from all list charges
    *
    * @param totalAmnt
    */
   public void setTotalAmnt(BigDecimal totalAmnt) {
      this.totalAmnt = totalAmnt;
   }

   /**
    * Return a list of charges
    *
    * @return
    */
   public List<Charge> getCharges() {
      return charges;
   }

   /**
    * Set the charges list
    *
    * @param charges
    */
   public void setCharges(List<Charge> charges) {
      this.charges = charges;

      // calculate the total amount if there is a list available

      if (this.charges != null) {
         try {
            lastLedgerDate = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1970");
         } catch (ParseException e) {
            e.printStackTrace();
         }
         lastStatementText = "";

         for (int i = 0; i < this.charges.size(); i++)
         {
            Charge chrg = this.charges.get(i);
            this.totalAmnt = this.totalAmnt.add(chrg.getAmount());

            // compare to get the latest ledger and effective date
            Date ledgerDate = chrg.getLedgerDate();
            String tranStatement = chrg.getStatementText();

            if(ledgerDate.compareTo(lastLedgerDate) > 0){
               //System.out.println("Date1 is after Date2");
               lastLedgerDate = ledgerDate;
               lastStatementText = tranStatement;
            }else if(ledgerDate.compareTo(lastLedgerDate) < 0){
               // skip setting earliest date, holding current value
               //System.out.println("Date1 is before Date2");
            }else if(ledgerDate.compareTo(lastLedgerDate) == 0){
               // chances of both dates being the same is minimum or maximum
               // might have to compare another field to make a determination
               //System.out.println("Date1 is equal to Date2");
            }else{
               //System.out.println("How to get here?");
            }
         }
      }
   }

   /**
    * Get the last ledger date
    * @return
    */
   public Date getLastLedgerDate() {
      return lastLedgerDate;
   }

   /**
    * Set the last ledger date
    * @param lastLedgerDate
    */
   public void setLastLedgerDate(Date lastLedgerDate) {
      this.lastLedgerDate = lastLedgerDate;
   }

   /**
    * Get the Last StatementText
    * @return
    */
   public String getLastStatementText() {
      return lastStatementText;
   }

   /**
    * Set the Last StatementText
    * @param lastStatementText
    */
   public void setLastStatementText(String lastStatementText) {
      this.lastStatementText = lastStatementText;
   }

   /**
    * Get the working set rows for pagination
    * @return
    */
   public String getWorkSetRows() {
      return workSetRows;
   }

   /**
    * Set the working set rows for pagination
    * @param workSetRows
    */
   public void setWorkSetRows(String workSetRows) {
      this.workSetRows = workSetRows;
   }

   /**
    * Get the offset minQueryDate for pagination
    * @return
    */
   public Date getMinQueryDate() {
      return minQueryDate;
   }

   /**
    * Set the offset minQueryDate for pagination
    * @param minQueryDate
    */
   public void setMinQueryDate(Date minQueryDate) {
      this.minQueryDate = minQueryDate;
   }

   /**
    * Get the limit maxQueryDate for pagination
    * @return
    */
   public Date getMaxQueryDate() {
      return maxQueryDate;
   }

   /**
    * Set the limit maxQueryDate for pagination
    * @param maxQueryDate
    */
   public void setMaxQueryDate(Date maxQueryDate) {
      this.maxQueryDate = maxQueryDate;
   }
}
