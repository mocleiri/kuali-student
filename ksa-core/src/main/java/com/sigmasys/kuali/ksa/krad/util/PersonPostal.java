package com.sigmasys.kuali.ksa.krad.util;

import com.sigmasys.kuali.ksa.model.PersonName;
import com.sigmasys.kuali.ksa.model.PostalAddress;

import static com.sigmasys.kuali.ksa.util.CommonUtils.nvl;

/**
 * User: dmulderink
 * Date: 4/25/12
 * Time: 8:17 AM
 */
public class PersonPostal {

   /**
    * Create a composite person name from PersonName record fields
    *
    * @param personName
    * @return
    */
   public String CreateCompositePersonName(PersonName personName) {

      StringBuilder personNameBuilder = new StringBuilder();

      if (personName != null) {

         // create the composite default person name

         personNameBuilder.append(nvl(personName.getLastName()));
         personNameBuilder.append(", ");
         personNameBuilder.append(nvl(personName.getFirstName()));
      }

      return personNameBuilder.toString();
   }

   /**
    * Create a composite postal address from PostalAddress record fields
    *
    * @param postalAddress
    * @return
    */
   public String CreateCompositePostalAddress(PostalAddress postalAddress) {

      StringBuilder postalAddressBuilder = new StringBuilder();

      // create the composite default postal address

      if (postalAddress != null) {
         postalAddressBuilder.append(nvl(postalAddress.getStreetAddress1()));
         postalAddressBuilder.append(" ");
         postalAddressBuilder.append(nvl(postalAddress.getCity()));
         postalAddressBuilder.append(", ");
         postalAddressBuilder.append(nvl(postalAddress.getState()));
         postalAddressBuilder.append(" ");
         postalAddressBuilder.append(nvl(postalAddress.getPostalCode()));
         postalAddressBuilder.append(" ");
         postalAddressBuilder.append(nvl(postalAddress.getCountry()));
      }

      return postalAddressBuilder.toString();
   }

}
