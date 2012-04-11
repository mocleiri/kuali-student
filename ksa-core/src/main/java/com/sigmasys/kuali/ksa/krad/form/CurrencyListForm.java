package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Currency;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;


/**
 * Currency list form
 *
 * @author Michael Ivanov
 */
public class CurrencyListForm extends UifFormBase {

   private List<Currency> currencies;

   private String iso;

   private String name;

   private String description;

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
    * Get the Name
    * @return
    */
   public String getName() {
      return name;
   }

   /**
    * Set the Name
    * @param name
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * Get the Description
    * @return
    */
   public String getDescription() {
      return description;
   }

   /**
    * Set  the Description
    * @param description
    */
   public void setDescription(String description) {
      this.description = description;
   }
}
