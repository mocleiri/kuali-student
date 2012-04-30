package com.sigmasys.kuali.ksa.gwt.server.impl;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.sigmasys.kuali.ksa.annotation.UrlMapping;
import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.model.GwtError;
import com.sigmasys.kuali.ksa.gwt.client.model.SearchCriteria;
import com.sigmasys.kuali.ksa.gwt.client.service.GwtAccountService;
import com.sigmasys.kuali.ksa.gwt.server.AbstractSearchService;
import com.sigmasys.kuali.ksa.gwt.server.SearchQueryBuilder;
import com.sigmasys.kuali.ksa.model.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * GwtAccountService implementation
 *
 * @author Michael Ivanov
 */
@UrlMapping(Constants.ACCOUNT_SERVICE_URL)
@Service("gwtAccountService")
@Transactional(readOnly = true)
public class GwtAccountServiceImpl extends AbstractSearchService implements GwtAccountService {

    private static final String select = "select distinct account ";

    private static final String from =
                    "from Account account " +
                    "left outer join fetch account.personNames person " +
                    "left outer join fetch account.postalAddresses postalAddress " +
                    "left outer join fetch account.electronicContacts contact " +
                    "left outer join fetch account.statusType statusType " +
                    "left outer join fetch account.latePeriod latePeriod ";

    private static final String where =
                    "where person.default = true and " +
                    "      postalAddress.default = true and " +
                    "      contact.default = true ";

    private static final String defaultOrder = "order by account.creationDate desc";


    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public PagingLoadResult<AccountModel> findAccounts(SearchCriteria searchCriteria, String sortDir,
                                                String sortField, int offset, int limit) throws GwtError {
        SearchQueryBuilder queryBuilder = new SearchQueryBuilder(select, from, where);
        queryBuilder.setDefaultSort(defaultOrder);
        // TODO: provide query builder customization for streetAddress
        return buildPagingLoadResult(queryBuilder, searchCriteria, sortDir, sortField, offset, limit);
    }

}
