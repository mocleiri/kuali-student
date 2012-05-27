package com.sigmasys.kuali.ksa.gwt.client.model;

/**
 * GWT version of Currency model.
 *
 * @author Michael Ivanov
 *         Date: 5/27/12
 *         Time: 11:55 AM
 */
public class CurrencyModel extends AuditableEntityModel {

    private static final String ISO = "iso";


    public String getIso() {
        return get(ISO);
    }

    public void setIso(String iso) {
        set(ISO, iso);
    }
}
