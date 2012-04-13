package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.PersonName;
import com.sigmasys.kuali.ksa.model.PostalAddress;
import com.sigmasys.kuali.ksa.model.Transaction;

import com.sigmasys.kuali.ksa.temp.AccountInfo;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TransOvrForm extends UifFormBase {
   private static final long serialVersionUID = -7525378097732916420L;

   // use this object as a query argument for matching transactions by student name
   private String studentLookupByName;

   // result set of matching persons and address postal information
   private List<Account> accountBrowseList;

   private AccountInfo accountInfo;

   // result set of matching persons
   private List<PersonName> personNameBrowseList;

   // result set of matching postal address per person
   private PostalAddress postalAddrPerPerson;

   // a list of Transactions - non-specific browse list
   private List<Transaction> transactionsBrowseList;

/*   private Transaction alertTransaction;

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
   }*/

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
    * Get the PersonNameBrowseList
    * A list of students matching the studentLookupByName
    * The query will use the is_default is 'Y'
    * @return
    */
   public List<PersonName> getPersonNameBrowseList() {
      return personNameBrowseList;
   }

   /**
    * Set the PersonNameBrowseList
    * A list of students matching the studentLookupByName
    * The query will use the is_default is 'Y'
    * @param personNameBrowseList
    */
   public void setPersonNameBrowseList(List<PersonName> personNameBrowseList) {
      this.personNameBrowseList = personNameBrowseList;
   }

   /**
    * Get the PostalAccress
    * A single record where the query uses is_default is 'Y'
    * @return
    */
   public PostalAddress getPostalAddrPerPerson() {
      return postalAddrPerPerson;
   }

   /**
    * Set the PostalAccress
    * A single record where the query uses is_default is 'Y'
    * @param postalAddrPerPerson
    */
   public void setPostalAddrPerPerson(PostalAddress postalAddrPerPerson) {
      this.postalAddrPerPerson = postalAddrPerPerson;
   }

   /**
    * Get the list of Transactions associated with the
    * @return
    */
   public List<Transaction> getTransactionsBrowseList() {
      return transactionsBrowseList;
   }

   /**
    *
    * @param transactionsBrowseList
    */
   public void setTransactionsBrowseList(List<Transaction> transactionsBrowseList) {
      this.transactionsBrowseList = transactionsBrowseList;
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

   public AccountInfo getAccountInfo() {
      return accountInfo;
   }

   public void setAccountInfo(AccountInfo accountInfo) {
      this.accountInfo = accountInfo;
   }
}
