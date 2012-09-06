package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;

import java.util.List;

/**
 * Created by: dmulderink on 8/29/12 at 12:58 PM
 */
public class BiographicInfoForm extends AbstractViewModel {

   private static final long serialVersionUID = -7525378097732916418L;

   // use this object as a query argument for matching transactions by student name
   private String bioSearchByAccount;

   // this is the composite person name first + middle and last name
   private String selectedAccountCompositePersonName;

   // result set of matching persons and address postal information
   private List<Account> accountBrowseList;

   // account ID and Bio information
   private String selectedId;

   private String kimNameType;

   private String title;

   private String firstName;

   private String middleName;

   private String lastName;

   private String suffix;

   private String isDefault;

   // PersonName fields concatenated
   private String compositePersonName;

   // PostalAddress fields concatenated
   private String compositePostalAddress;

   /*
      Get/Set methods
    */

   public String getBioSearchByAccount() {
      return bioSearchByAccount;
   }

   public void setBioSearchByAccount(String bioSearchByAccount) {
      this.bioSearchByAccount = bioSearchByAccount;
   }

   public String getSelectedAccountCompositePersonName() {
      return selectedAccountCompositePersonName;
   }

   public void setSelectedAccountCompositePersonName(String selectedAccountCompositePersonName) {
      this.selectedAccountCompositePersonName = selectedAccountCompositePersonName;
   }

   public List<Account> getAccountBrowseList() {
      return accountBrowseList;
   }

   public void setAccountBrowseList(List<Account> accountBrowseList) {
      this.accountBrowseList = accountBrowseList;
   }

   public String getSelectedId() {
      return selectedId;
   }

   public void setSelectedId(String selectedId) {
      this.selectedId = selectedId;
   }

   public String getKimNameType() {
      return kimNameType;
   }

   public void setKimNameType(String kimNameType) {
      this.kimNameType = kimNameType;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getMiddleName() {
      return middleName;
   }

   public void setMiddleName(String middleName) {
      this.middleName = middleName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getSuffix() {
      return suffix;
   }

   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }

   public String getDefault() {
      return isDefault;
   }

   public void setDefault(String aDefault) {
      isDefault = aDefault;
   }

   public String getCompositePersonName() {
      return compositePersonName;
   }

   public void setCompositePersonName(String compositePersonName) {
      this.compositePersonName = compositePersonName;
   }
}
