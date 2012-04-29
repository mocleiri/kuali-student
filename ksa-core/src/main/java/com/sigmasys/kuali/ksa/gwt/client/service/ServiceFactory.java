package com.sigmasys.kuali.ksa.gwt.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.sigmasys.kuali.ksa.model.Constants;

/**
 * ServiceFactory creates GWT async services.
 *
 * @author Michael Ivanov
 */
@SuppressWarnings("unchecked")
public class ServiceFactory {

    private static final GwtAccountServiceAsync accountService;

    // Initialization of the remote services
    static {
        accountService = initService(GWT.create(GwtAccountService.class), Constants.ACCOUNT_SERVICE_URL);
    }

    private ServiceFactory() {
    }

    protected static <T> T initService(Object asyncService, String serviceUrl) {
        ((ServiceDefTarget) asyncService).setServiceEntryPoint(serviceUrl);
        return (T) asyncService;
    }

    public static GwtAccountServiceAsync getAccountService() {
        return accountService;
    }

}
