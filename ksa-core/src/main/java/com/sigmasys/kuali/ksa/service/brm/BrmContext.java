package com.sigmasys.kuali.ksa.service.brm;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.service.*;
import com.sigmasys.kuali.ksa.service.atp.AtpService;
import com.sigmasys.kuali.ksa.service.hold.HoldService;
import com.sigmasys.kuali.ksa.util.ContextUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * BRM (Business Rules Management) Context.
 *
 * @author Michael Ivanov
 */
public class BrmContext implements Serializable {

    private Account account;

    private Map<String, Object> attributes = new HashMap<String, Object>();
    private Map<String, Object> globalVariables = new HashMap<String, Object>();

    public boolean isInitialized() {
        return account != null && attributes != null && globalVariables != null;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getGlobalVariables() {
        return globalVariables;
    }

    public void setGlobalVariables(Map<String, Object> globalVariables) {
        this.globalVariables = globalVariables;
    }

    public AccountService getAccountService() {
        return getService(AccountService.class);
    }

    public TransactionService getTransactionService() {
        return getService(TransactionService.class);
    }

    public GeneralLedgerService getGeneralLedgerService() {
        return getService(GeneralLedgerService.class);
    }

    public PaymentService getPaymentService() {
        return getService(PaymentService.class);
    }

    public BrmPaymentService getBrmPaymentService() {
        return getService(BrmPaymentService.class);
    }

    public HoldService getHoldService() {
        return getService(HoldService.class);
    }

    public AtpService getAtpService() {
        return getService(AtpService.class);
    }

    public InformationService getInformationService() {
        return getService(InformationService.class);
    }

    protected <T> T getService(Class<T> serviceType) {
        T service = ContextUtils.getBean(serviceType);
        if (service == null) {
            throw new IllegalStateException("Cannot find " + serviceType.getName() + " in the application context");
        }
        return service;
    }

}
