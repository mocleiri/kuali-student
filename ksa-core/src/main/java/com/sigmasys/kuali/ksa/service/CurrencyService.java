package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.model.Currency;

import java.util.List;

/**
 * Currency Service
 * <p/>
 *
 * @author Michael Ivanov
 */
public interface CurrencyService {

    /**
     * Returns Currency by ID
     *
     * @param id Currency ID
     * @return Currency instance
     */
    Currency getCurrency(Long id);

    /**
     * Returns Currency by ISO symbol
     *
     * @param iso ISO currency name
     * @return Currency instance
     */
    Currency getCurrency(String iso);

    /**
     * Returns all currencies sorted by ISO in the descendant order
     *
     * @return List of currencies
     */
    List<Currency> getCurrencies();

    /**
     * Persists the currency in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param currency Currency instance
     * @return Currency ID
     */
    Long persistCurrency(Currency currency);

    /**
     * Removes the currency from the database.
     *
     * @param id Currency ID
     * @return true if the Currency entity has been deleted
     */
    boolean deleteCurrency(Long id);

}
