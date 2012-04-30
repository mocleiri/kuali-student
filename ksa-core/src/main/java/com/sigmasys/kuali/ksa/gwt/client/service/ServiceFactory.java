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

    private static final GwtConfigServiceAsync configService;
    private static final GwtAccountServiceAsync accountService;

    // Initialization of the remote services
    static {
        configService = initService(GWT.create(GwtConfigService.class), Constants.CONFIG_SERVICE_URL);
        accountService = initService(GWT.create(GwtAccountService.class), Constants.ACCOUNT_SERVICE_URL);
    }

    private ServiceFactory() {
    }

    protected static <T> T initService(Object asyncService, String serviceUrl) {
        ((ServiceDefTarget) asyncService).setServiceEntryPoint(serviceUrl);
        return (T) asyncService;
    }

    public static GwtConfigServiceAsync getConfigService() {
           return configService;
    }

    public static GwtAccountServiceAsync getAccountService() {
        return accountService;
    }

}
