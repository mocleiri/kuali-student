package com.sigmasys.kuali.ksa.gwt.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.sigmasys.kuali.ksa.gwt.client.model.ReferenceData;
import com.sigmasys.kuali.ksa.gwt.client.service.GenericCallback;
import com.sigmasys.kuali.ksa.gwt.client.service.GwtErrorHandler;
import com.sigmasys.kuali.ksa.gwt.client.service.ServiceFactory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class KsaEntryPoint implements EntryPoint {

    private MessageBox waitingBox;
    private ReferenceData referenceData;

    public KsaEntryPoint() {
        Registry.register(KsaEntryPoint.class.getName(), this);
    }

    public void onModuleLoad() {

        Log.debug("onModuleLoad Start");

        waitingBox = MessageBox.wait("", "Loading workspace...", "");

        Log.debug("getModuleBaseURL: " + GWT.getModuleBaseURL());
        Log.debug("getHostPageBaseURL: " + GWT.getHostPageBaseURL());

        // Initializing reference data
        loadReferenceData();

        Log.debug("onModuleLoad Finish");
    }

    public static KsaEntryPoint getInstance() {
        return Registry.get(KsaEntryPoint.class.getName());
    }


    protected void loadReferenceData() {
        try {
            Log.debug("Loading Reference data");
            // Start loading reference data asynchronously
            ServiceFactory.getConfigService().getReferenceData(new GenericCallback<ReferenceData>() {
                public void onSuccess(ReferenceData referenceData) {
                    try {
                        Log.debug("Load Reference data done");
                        setReferenceData(referenceData);
                        closeWaitingBox();
                    } catch (Throwable t) {
                        handleError(t);
                    }
                }

                public void onFailure(Throwable t) {
                    handleError(t);
                }
            });
        } catch (Throwable t) {
            handleError(t);
        }
    }

    private void closeWaitingBox() {
        if (waitingBox != null && waitingBox.isVisible()) {
            waitingBox.close();
        }
    }

    private void handleError(Throwable t) {
        closeWaitingBox();
        GwtErrorHandler.error("Cannot retrieve reference data. " + t.getMessage(), t);
    }

    public void setReferenceData(ReferenceData referenceData) {
        this.referenceData = referenceData;
    }

    public ReferenceData getReferenceData() {
        return referenceData;
    }
}
