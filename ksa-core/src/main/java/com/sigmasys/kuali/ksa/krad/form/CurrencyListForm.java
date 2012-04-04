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


    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }
}
