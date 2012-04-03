package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.model.Deferment;
import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * Currency service JPA implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("currencyService")
@Transactional(readOnly = true)
public class CurrencyServiceImpl implements CurrencyService {

    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;


    /**
     * Returns Currency by ID
     *
     * @param id Currency ID
     * @return Currency instance
     */
    @Override
    public Currency getCurrency(Long id) {
        return em.find(Currency.class, id);
    }

    /**
     * Returns Currency by ISO symbol
     *
     * @param iso ISO currency name
     * @return Currency instance
     */
    @Override
    public Currency getCurrency(String iso) {
        Query query = em.createQuery("select c from Currency c where upper(c.iso) = upper(:iso)");
        query.setParameter("iso", iso);
        List<Currency> currencies = query.getResultList();
        if (currencies != null && !currencies.isEmpty()) {
            return currencies.get(0);
        }
        throw new IllegalArgumentException("Currency with ISO = '" + iso + "' does not exist");
    }

    /**
     * Returns all currencies sorted by ISO in the descendant order
     *
     * @return List of currencies
     */
    @Override
    public List<Currency> getCurrencies() {
        Query query = em.createQuery("select c from Currency c order by c.iso desc");
        return query.getResultList();
    }

    /**
     * Persists the currency in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param currency Currency instance
     * @return Currency ID
     */
    @Override
    @Transactional(readOnly = false)
    public Long persistCurrency(Currency currency) {
        if (currency.getId() == null) {
            em.persist(currency);
        } else {
            em.merge(currency);
        }
        return currency.getId();
    }

}
