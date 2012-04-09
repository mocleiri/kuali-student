package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Currency service JPA implementation.
 * <p/>
 *
 * @author Tim Bornholtz
 */
@Service("accountService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class AccountServiceImpl extends GenericPersistenceService implements AccountService {

    @Override
    public void rebalance(Boolean ignoreDeferment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void ageDebt(Boolean ignoreDeferment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getDueBalance(Boolean ignoreDeferment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getOutstandingBalance(Boolean ignoreDeferment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getUnallocatedBalance() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getDeferredAmount() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void makeEffective() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void paymentApplication() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createAllocation(Transaction transaction1, Transaction transaction2, BigDecimal allocationAmount) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createLockedAllocation(Transaction transaction1, Transaction transaction2, BigDecimal allocationAmount) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean doesAccountExist(String accountIdentifer) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean doesKsaAccountExist(String accountIdentifier) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Account instantiateKsaAccount() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Account instantiateKsaAccount(String accountIdentifier) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
