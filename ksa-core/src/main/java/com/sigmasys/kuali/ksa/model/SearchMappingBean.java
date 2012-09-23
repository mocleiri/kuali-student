package com.sigmasys.kuali.ksa.model;


import java.io.Serializable;
import java.util.Map;

/**
 * Simple POJO that defines URL mapping between "searchType" and action URLs
 *
 */
public class SearchMappingBean implements Serializable {

    // Map of "searchType" <-> "actionUrl"
    private Map<String, String> mapping;

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }
}
