package com.sigmasys.kuali.ksa.gwt.server.impl;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.sigmasys.kuali.ksa.annotation.UrlMapping;
import com.sigmasys.kuali.ksa.gwt.client.model.*;
import com.sigmasys.kuali.ksa.gwt.client.model.TransactionType;
import com.sigmasys.kuali.ksa.gwt.client.service.GwtTransactionService;
import com.sigmasys.kuali.ksa.gwt.server.AbstractSearchService;
import com.sigmasys.kuali.ksa.gwt.server.SearchQueryBuilder;
import com.sigmasys.kuali.ksa.gwt.server.TransactionColumnMapper;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * GwtTransactionService implementation
 *
 * @author Michael Ivanov
 */
@UrlMapping(Constants.TRANSACTION_SERVICE_URL)
@Service("gwtTransactionService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class GwtTransactionServiceImpl extends AbstractSearchService implements GwtTransactionService {

    private static final Log logger = LogFactory.getLog(GwtTransactionServiceImpl.class);

    private static final String select = "select transaction";

    private static final String from =
            "from Transaction transaction " +
                    "left outer join fetch transaction.transactionType type " +
                    "left outer join fetch transaction.account account " +
                    "left outer join fetch transaction.currency currency " +
                    "left outer join fetch transaction.rollup rollup " +
                    "left outer join fetch transaction.document document";

    private static final String defaultOrder = "transaction.effectiveDate desc";


    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;

    @Autowired
    private CurrencyService currencyService;


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public PagingLoadResult<TransactionModel> findTransactions(SearchCriteria searchCriteria, String sortDir,
                                                               String sortField, int offset, int limit) throws GwtError {
        logger.info("sortDir = " + sortDir + ", sortField = " + sortField);
        SearchQueryBuilder queryBuilder = new SearchQueryBuilder(select, from);
        queryBuilder.setDefaultSort(defaultOrder);
        queryBuilder.setSortMapping(new TransactionColumnMapper());
        // TODO: provide query builder customization for transaction types ?
        PagingLoadResult<Transaction> loadResult =
                buildPagingLoadResult(queryBuilder, searchCriteria, sortDir, sortField, offset, limit);

        List<TransactionModel> transactions = new ArrayList<TransactionModel>(loadResult.getData().size());
        for (Transaction transaction : loadResult.getData()) {
            transactions.add(createModelFrom(transaction));
        }
        return new PagingLoadResultImpl<TransactionModel>(transactions, loadResult.getOffset(), loadResult.getTotalLength());
    }

    private TransactionModel createModelFrom(Transaction transaction) {

        TransactionModel model = new TransactionModel();
        model.setId(transaction.getId());
        model.setExternalId(transaction.getExternalId());
        model.setTypeId(transaction.getTransactionType().getId().getId());
        model.setTypeSubCode(transaction.getTransactionType().getId().getSubCode());

        model.setAmount(transaction.getAmount() != null ? transaction.getAmount().doubleValue() : 0.0);
        model.setNativeAmount(transaction.getNativeAmount() != null ? transaction.getNativeAmount().doubleValue() : 0.0);
        model.setAllocatedAmount(transaction.getAllocatedAmount() != null ?
                transaction.getAllocatedAmount().doubleValue() : 0.0);
        model.setLockedAllocatedAmount(transaction.getLockedAllocatedAmount() != null ?
                transaction.getAllocatedAmount().doubleValue() : 0.0);

        model.setAccountId(transaction.getAccount().getId());
        model.setCurrencyCode(transaction.getCurrency().getIso());

        model.setEffectiveDate(transaction.getEffectiveDate());
        model.setLedgerDate(transaction.getLedgerDate());
        model.setOriginationDate(transaction.getOriginationDate());
        model.setGlEntryGenerated(transaction.isGlEntryGenerated());
        model.setInternal(transaction.isInternal());
        model.setRefundRule(transaction.getRefundRule());
        model.setStatementText(transaction.getStatementText());
        model.setResponsibleEntity(transaction.getResponsibleEntity());

        if (transaction instanceof Charge) {
            model.setType(TransactionType.CHARGE);
        } else if (transaction instanceof Payment) {
            model.setType(TransactionType.PAYMENT);
        } else if (transaction instanceof Deferment) {
            model.setType(TransactionType.DEFERMENT);
        }

        return model;
    }

    @Override
    public List<String> getExistingCurrencyCodes() throws GwtError {
        List<Currency> currencies = currencyService.getCurrencies();
        List<String> currencyCodes = new ArrayList<String>(currencies.size());
        for ( Currency currency : currencies) {
            currencyCodes.add(currency.getIso());
        }
        return currencyCodes;
    }

}
