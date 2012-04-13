package com.sigmasys.kuali.ksa.temp;


import com.sigmasys.kuali.ksa.model.PersonName;
import com.sigmasys.kuali.ksa.model.PostalAddress;

import java.util.List;
import java.util.Set;

/**
 * User: dmulderink
 * Date: 4/13/12
 * Time: 8:36 AM
 */
public class AccountInfo {

   private List<PersonName> personNames;

   private List<PostalAddress> postalAddresses;

   /**
    * Collection of associated person names
    */
   public List<PersonName> getPersonNames() {
      return personNames;
   }

   public void setPersonNames(List<PersonName> personNames) {
      this.personNames = personNames;
   }

   /**
    * Collection of associated postal addresses
    */
   public List<PostalAddress> getPostalAddresses() {
      return postalAddresses;
   }

   public void setPostalAddresses(List<PostalAddress> postalAddresses) {
      this.postalAddresses = postalAddresses;
   }
}
