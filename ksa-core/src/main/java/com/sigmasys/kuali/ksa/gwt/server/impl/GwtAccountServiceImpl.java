package com.sigmasys.kuali.ksa.gwt.server.impl;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.sigmasys.kuali.ksa.annotation.UrlMapping;
import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.model.GwtError;
import com.sigmasys.kuali.ksa.gwt.client.model.PagingLoadResultImpl;
import com.sigmasys.kuali.ksa.gwt.client.model.SearchCriteria;
import com.sigmasys.kuali.ksa.gwt.client.service.GwtAccountService;
import com.sigmasys.kuali.ksa.gwt.server.AbstractSearchService;
import com.sigmasys.kuali.ksa.gwt.server.SearchQueryBuilder;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.LatePeriod;
import com.sigmasys.kuali.ksa.model.PostalAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

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
            "      person.default = true and " +
                    "      postalAddress.default = true and " +
                    "      contact.default = true ";

    private static final String defaultOrder = "account.creationDate desc";


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
        PagingLoadResult<Account> loadResult =
                buildPagingLoadResult(queryBuilder, searchCriteria, sortDir, sortField, offset, limit);

        List<AccountModel> accounts = new ArrayList<AccountModel>(loadResult.getData().size());
        for (Account account : loadResult.getData()) {
            accounts.add(createModelFrom(account));
        }
        return new PagingLoadResultImpl<AccountModel>(accounts, loadResult.getOffset(), loadResult.getTotalLength());
    }

    private AccountModel createModelFrom(Account account) {

        AccountModel model = new AccountModel();

        model.setId(account.getId());
        model.setEntityId(account.getEntityId());
        model.setCreationDate(account.getCreationDate());
        model.setKimAccount(account.isKimAccount());
        model.setLastKimUpdate(account.getLastKimUpdate());
        model.setCreditLimit(account.getCreditLimit());

        LatePeriod latePeriod = account.getLatePeriod();
        model.setDaysLate1(latePeriod.getDaysLate1());
        model.setDaysLate2(latePeriod.getDaysLate2());
        model.setDaysLate3(latePeriod.getDaysLate3());

        PostalAddress address = account.getDefaultPostalAddress();
        model.setCountry(address.getCountry());
        model.setPostalCode(address.getPostalCode());
        model.setState(address.getState());
        model.setCity(address.getCity());

        String streetAddress = address.getStreetAddress1();
        if (streetAddress != null) {
            String streetAddress2 = address.getStreetAddress2();
            if (streetAddress2 != null && !streetAddress2.trim().isEmpty()) {
                streetAddress += " " + streetAddress2;
            }
            String streetAddress3 = address.getStreetAddress3();
            if (streetAddress3 != null && !streetAddress3.trim().isEmpty()) {
                streetAddress += " " + streetAddress3;
            }

        }

        model.setStreetAddress(streetAddress);

        return model;
    }

}
