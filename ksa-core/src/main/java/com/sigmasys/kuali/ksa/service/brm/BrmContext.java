package com.sigmasys.kuali.ksa.service.brm;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.service.*;
import com.sigmasys.kuali.ksa.util.ContextUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * BRM (Business Rules Management) Context.
 *
 * @author Michael Ivanov
 */
public class BrmContext implements Serializable {

    private Account account;

    private Map<String, Object> parameters;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public AccountService getAccountService() {
        return ContextUtils.getBean(AccountService.class);
    }

    public TransactionService getTransactionService() {
        return ContextUtils.getBean(TransactionService.class);
    }

    public GeneralLedgerService getGeneralLedgerService() {
        return ContextUtils.getBean(GeneralLedgerService.class);
    }

    public PaymentService getPaymentService() {
        return ContextUtils.getBean(PaymentService.class);
    }

    public FeeManagementService getFeeManagementService() {
        return ContextUtils.getBean(FeeManagementService.class);
    }

}
