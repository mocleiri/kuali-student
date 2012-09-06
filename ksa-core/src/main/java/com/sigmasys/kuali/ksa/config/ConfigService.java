package com.sigmasys.kuali.ksa.config;

import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.InitialParameter;
import com.sigmasys.kuali.ksa.model.LocalizedString;
import com.sigmasys.kuali.ksa.service.LocalizationService;
import com.sigmasys.kuali.ksa.util.LocaleUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param locale java.util.Locale instance
     * @return map of locale-aware parameters
     */
    public Map<String, String> getLocalizedParameters(Locale locale);

    public String getInitialParameter(String name);

    public Map<String, String> getInitialParameters();

    public Integer updateInitialParameters(List<InitialParameter> params);

    public Integer deleteInitialParameters(Set<String> paramNames);

    public List<InitialParameter> getInitialParameterList();

    public Map<String, String> refreshInitialParameters();

    public void changeLoggingLevel(Map<String, String> loggers);

    public Map<String, String> getLoggingLevel(String[] loggers);

}
