package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Charge;

import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.model.Payment;
import org.kuali.rice.krad.web.form.UifFormBase;
import java.util.List;

public class TransOvrForm extends UifFormBase {
   private static final long serialVersionUID = -7525378097732916420L;

   // use this object as a query argument for matching transactions by student name
   private String studentLookupByName;

   private String selectedPersonName;

   // result set of matching persons and address postal information
   private List<Account> accountBrowseList;

   private List<Charge> chargeList;

   // result set of charges
   private List<Payment> paymentList;

   // a single charge
   private Charge charge;

   // a single payment
   private Payment payment;

   // Currency stuff
   private List<Currency> currencies;

   private Currency currency;

   private String iso;

   private String currencyName;

   private String currencyDescription;

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
    * Get the selected person name
    * @return
    */
   public String getSelectedPersonName() {
      return selectedPersonName;
   }

   /**
    * Set the selected person name
    * @param selectedPersonName
    */
   public void setSelectedPersonName(String selectedPersonName) {
      this.selectedPersonName = selectedPersonName;
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

   /**
    * Get the list of Payments found via a selected Account
    * @return
    */
   public List<Payment> getPaymentList() {
      return paymentList;
   }

   /**
    * Set the list of Payments found via a selected Account
    * @param paymentList
    */
   public void setPaymentList(List<Payment> paymentList) {
      this.paymentList = paymentList;
   }

   /**
    * Get a charge object
    * @return
    */
   public Charge getCharge() {
      return charge;
   }

   /**
    * Set a charge object
    * @param charge
    */
   public void setCharge(Charge charge) {
      this.charge = charge;
   }

   /**
    * Get a Payment object
    * @return
    */
   public Payment getPayment() {
      return payment;
   }

   /**
    * Set a Payment object
    * @param payment
    */
   public void setPayment(Payment payment) {
      this.payment = payment;
   }

   // Currency get/Set methods

   /**
    * Get the list of Currency objects
    * @return
    */
   public List<Currency> getCurrencies() {
      return currencies;
   }

   /**
    * Set the list of Currency objects
    * @param currencies
    */
   public void setCurrencies(List<Currency> currencies) {
      this.currencies = currencies;
   }

   /**
    * Get the Currency model object
    * @return
    */
   public Currency getCurrency() {
      return currency;
   }

   /**
    * Set teh Currency model object
    * @param currency
    */
   public void setCurrency(Currency currency) {
      this.currency = currency;
   }

   /**
    * Get the ISO symbol
    * @return
    */
   public String getIso() {
      return iso;
   }

   /**
    * Set the ISO symbol
    * @param iso
    */
   public void setIso(String iso) {
      this.iso = iso;
   }

   /**
    * Get the currencyName
    * @return
    */
   public String getCurrencyName() {
      return currencyName;
   }

   /**
    * Set the Name
    * @param currencyName
    */
   public void setCurrencyName(String currencyName) {
      this.currencyName = currencyName;
   }

   /**
    * Get the currency description
    * @return
    */
   public String getCurrencyDescription() {
      return currencyDescription;
   }

   /**
    * Set  the currency description
    * @param currencyDescription
    */
   public void setCurrencyDescription(String currencyDescription) {
      this.currencyDescription = currencyDescription;
   }
}
