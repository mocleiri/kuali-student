package com.sigmasys.kuali.ksa.service.brm;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.FeeManagementService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.util.ContextUtils;

import java.io.Serializable;

/**
 * BRM (Business Rules Management) Context.
 *
 * @author Michael Ivanov
 */
public class BrmContext implements Serializable {

    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public AccountService getAccountService() {
        return ContextUtils.getBean(AccountService.class);
    }

    public TransactionService getTransactionService() {
        return ContextUtils.getBean(TransactionService.class);
    }

    public FeeManagementService getFeeManagementService() {
        return ContextUtils.getBean(FeeManagementService.class);
    }

}
