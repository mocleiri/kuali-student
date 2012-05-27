package com.sigmasys.kuali.ksa.gwt.server.impl;

import com.sigmasys.kuali.ksa.gwt.client.model.CurrencyModel;
import com.sigmasys.kuali.ksa.gwt.client.service.GwtCurrencyService;
import com.sigmasys.kuali.ksa.gwt.server.AbstractRemoteService;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * GWT Currency service implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("gwtCurrencyService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class GwtCurrencyServiceImpl extends AbstractRemoteService implements GwtCurrencyService {

    @Autowired
    private CurrencyService currencyService;


    /**
     * Returns Currency by ID
     *
     * @param id Currency ID
     * @return Currency instance
     */
    @Override
    public CurrencyModel getCurrency(Long id) {
        return createModel(currencyService.getCurrency(id));
    }

    /**
     * Returns Currency by ISO symbol
     *
     * @param iso ISO currency name
     * @return CurrencyModel instance
     */
    @Override
    public CurrencyModel getCurrency(String iso) {
        return createModel(currencyService.getCurrency(iso));
    }

    /**
     * Returns all currencies sorted by ISO in the ascending order
     *
     * @return List of currencies
     */
    @Override
    public List<CurrencyModel> getCurrencies() {
        List<Currency> currencies = currencyService.getCurrencies();
        List<CurrencyModel> models = new ArrayList<CurrencyModel>(currencies != null ? currencies.size() : 0);
        if (currencies != null) {
            for (Currency currency : currencies) {
                models.add(createModel(currency));
            }
        }
        return models;
    }

    /**
     * Persists the currency in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param currency CurrencyModel instance
     * @return Currency ID
     */
    @Override
    @Transactional(readOnly = false)
    public Long persistCurrency(CurrencyModel currency) {
        return currencyService.persistCurrency(createModel(currency));
    }

    /**
     * Removes the currency from the database.
     *
     * @param id Currency ID
     * @return true if the Currency entity has been deleted
     */
    @Override
    @Transactional(readOnly = false)
    public boolean deleteCurrency(Long id) {
        return currencyService.deleteCurrency(id);
    }

    private CurrencyModel createModel(Currency currency) {
        CurrencyModel model = new CurrencyModel();
        model.setId(currency.getId());
        model.setIso(currency.getIso());
        model.setName(currency.getName());
        model.setDescription(currency.getDescription());
        model.setCreatorId(currency.getCreatorId());
        model.setEditorId(currency.getEditorId());
        model.setLastUpdate(currency.getLastUpdate());
        return model;
    }

    private Currency createModel(CurrencyModel currency) {
        Currency model = new Currency();
        model.setId(currency.getId());
        model.setIso(currency.getIso());
        model.setName(currency.getName());
        model.setDescription(currency.getDescription());
        model.setCreatorId(currency.getCreatorId());
        model.setEditorId(currency.getEditorId());
        model.setLastUpdate(currency.getLastUpdate());
        return model;
    }

}
