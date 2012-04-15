package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Charge;

import org.kuali.rice.krad.web.form.UifFormBase;
import java.util.List;

public class TransOvrForm extends UifFormBase {
   private static final long serialVersionUID = -7525378097732916420L;

   // use this object as a query argument for matching transactions by student name
   private String studentLookupByName;

   // result set of matching persons and address postal information
   private List<Account> accountBrowseList;

   private List<Charge> chargeList;

   /**
    * Get the student name
    * Possible uses is a query match for transactions
    * The value can be a partial matching name
    * @return
    */
   public String getStudentLookupByName() {
      return studentLookupByName;
   }

   /**
    * Set the student lookup name
    * Possible uses is a query match for transactions
    * The value can be a partial matching name
    * @param studentLookupByName
    */
   public void setStudentLookupByName(String studentLookupByName) {
      this.studentLookupByName = studentLookupByName;
   }

   /**
    * Get the accountBrowseList
    * Encapsulates Person and Address model
    * @return
    */
   public List<Account> getAccountBrowseList() {
      return accountBrowseList;
   }

   /**
    * Set the accountBrowseList
    * Encapsulates Person and Address model
    * @param accountBrowseList
    */
   public void setAccountBrowseList(List<Account> accountBrowseList) {
      this.accountBrowseList = accountBrowseList;
   }

   /**
    * Get the list of Charges found via a selected Account
    * @return
    */
   public List<Charge> getChargeList() {
      return chargeList;
   }

   /**
    * Set the list of Charges found via a selected Account
    * @param chargeList
    */
   public void setChargeList(List<Charge> chargeList) {
      this.chargeList = chargeList;
   }
}
