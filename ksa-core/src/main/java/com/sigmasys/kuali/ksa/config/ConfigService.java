package com.sigmasys.kuali.ksa.config;

import com.sigmasys.kuali.ksa.model.InitialParameter;

import java.util.*;

/**
 * KSA Config Service
 *
 * @author Michael Ivanov
 *         Date: 3/22/12
 *         Time: 4:58 PM
 */
public interface ConfigService {

    /**
     * Returns the localized parameters for the given locale
     *
     * @param locale java.util.Locale instance
     * @return map of locale-aware parameters
     */
    public Map<String, String> getLocalizedParameters(Locale locale);

    public String getParameter(String name);

    public Map<String, String> getInitialParameters();

    public Integer updateParameters(List<InitialParameter> params);

    public Integer deleteParameters(Set<String> paramNames);

    public List<InitialParameter> getParameterList();

    public Map<String, String> refreshParameters();

    public void changeLoggingLevel(Map<String, String> loggers);

    public Map<String, String> getLoggingLevel(String[] loggers);

}
