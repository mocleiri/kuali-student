package com.sigmasys.kuali.ksa.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sigmasys.kuali.ksa.gwt.client.model.CurrencyModel;

import java.util.List;

public interface GwtCurrencyServiceAsync {

    /**
     * Returns Currency by ID
     *
     * @param id Currency ID
     * @throws com.sigmasys.kuali.ksa.gwt.client.model.GwtError
     *
     */
    void getCurrency(Long id, AsyncCallback<CurrencyModel> callback);

    /**
     * Returns Currency by ISO symbol
     *
     * @param code ISO currency code
     * @throws com.sigmasys.kuali.ksa.gwt.client.model.GwtError
     *
     */
    void getCurrency(String code, AsyncCallback<CurrencyModel> callback);

    /**
     * Returns all currencies sorted by ISO in the ascending order
     *
     * @throws com.sigmasys.kuali.ksa.gwt.client.model.GwtError
     *
     */
    void getCurrencies(AsyncCallback<List<CurrencyModel>> callback);

    /**
     * Persists the currency in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param currency CurrencyModel instance
     * @throws com.sigmasys.kuali.ksa.gwt.client.model.GwtError
     *
     */
    void persistCurrency(CurrencyModel currency, AsyncCallback<Long> callback);

    /**
     * Removes the currency from the database.
     *
     * @param id Currency ID
     * @throws com.sigmasys.kuali.ksa.gwt.client.model.GwtError
     *
     */
    void deleteCurrency(Long id, AsyncCallback<Boolean> callback);
}
