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

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

   public String getIso() {
      return iso;
   }

   public void setIso(String iso) {
      this.iso = iso;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }
}
