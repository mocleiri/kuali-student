package com.sigmasys.kuali.ksa.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.sigmasys.kuali.ksa.gwt.client.model.CurrencyModel;
import com.sigmasys.kuali.ksa.gwt.client.model.GwtError;

import java.util.List;

/**
 * GWT Currency Service
 * <p/>
 *
 * @author Michael Ivanov
 */
public interface GwtCurrencyService extends RemoteService {

    /**
     * Returns Currency by ID
     *
     * @param id Currency ID
     * @return CurrencyModel instance
     * @throws GwtError
     */
    CurrencyModel getCurrency(Long id) throws GwtError;

    /**
     * Returns Currency by ISO symbol
     *
     * @param code ISO currency code
     * @return CurrencyModel instance
     * @throws GwtError
     */
    CurrencyModel getCurrency(String code) throws GwtError;

    /**
     * Returns all currencies sorted by ISO in the ascending order
     *
     * @return List of currencies
     * @throws GwtError
     */
    List<CurrencyModel> getCurrencies() throws GwtError;

    /**
     * Persists the currency in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param currency CurrencyModel instance
     * @return Currency ID
     * @throws GwtError
     */
    Long persistCurrency(CurrencyModel currency) throws GwtError;

    /**
     * Removes the currency from the database.
     *
     * @param id Currency ID
     * @return true if the Currency entity has been deleted
     * @throws GwtError
     */
    boolean deleteCurrency(Long id) throws GwtError;

}
