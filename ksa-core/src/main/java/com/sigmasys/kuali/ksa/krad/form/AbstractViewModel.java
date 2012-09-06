package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.UserPreference;
import com.sigmasys.kuali.ksa.service.UserPreferenceService;
import com.sigmasys.kuali.ksa.service.UserSessionManager;
import com.sigmasys.kuali.ksa.util.ContextUtils;
import com.sigmasys.kuali.ksa.util.LocaleUtils;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.krad.web.form.UifFormBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The base abstract form. It is supposed to be extended by KSA subclasses.
 *
 * @author Michael Ivanov
 */
@SuppressWarnings("unchecked")
public abstract class AbstractViewModel extends UifFormBase {

    protected final Log logger = LogFactory.getLog(getClass());

    private static final String USER_PREF_ATTR_NAME = "userPreferencesSessionAttr";
    private static final String LOCALIZED_PARAMS_ATTR_NAME = "localizedParamsSessionAttr";


    protected ConfigService getConfigService() {
        return ContextUtils.getBean(ConfigService.class);
    }

    protected UserPreferenceService getUserPreferenceService() {
        return ContextUtils.getBean(UserPreferenceService.class);
    }

    protected UserSessionManager getUserSessionManager() {
        return ContextUtils.getBean(UserSessionManager.class);
    }

    protected HttpSession getSession() {
        HttpServletRequest request = RequestUtils.getThreadRequest();
        if (request == null) {
            throw new IllegalStateException("HttpServletRequest is null");
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new IllegalStateException("HttpSession has not been initialized");
        }
        return session;
    }


    protected Map<String, String> getUserPreferences() {
        Map<String, String> preferencesMap = (Map<String, String>) getSession().getAttribute(USER_PREF_ATTR_NAME);
        if (preferencesMap == null) {
            String userId = getUserSessionManager().getUserId(RequestUtils.getThreadRequest());
            if (userId == null) {
                throw new IllegalStateException("User ID is null");
            }
            List<UserPreference> userPreferences = getUserPreferenceService().getUserPreferences(userId);
            if (userPreferences != null) {
                preferencesMap = new HashMap<String, String>(userPreferences.size());
                for (UserPreference userPreference : userPreferences) {
                    preferencesMap.put(userPreference.getName(), userPreference.getValue());
                }
                getSession().setAttribute(USER_PREF_ATTR_NAME, userPreferences);
            }
        }
        return preferencesMap;
    }

    /**
     * Returns the locale-aware string value using the locale from user preferences
     * or system wide LocaleUtils for the current default locale
     *
     * @param key parameter name
     * @return parameter value
     */
    public String getLValue(String key) {
        Map<String, String> params = (Map<String, String>) getSession().getAttribute(LOCALIZED_PARAMS_ATTR_NAME);
        if (params == null) {
            Map<String, String> userPreferences = getUserPreferences();
            if (userPreferences != null) {
                String localeLang = userPreferences.get(Constants.LOCALE_LANG_PARAM_NAME);
                String localeCountry = userPreferences.get(Constants.LOCALE_COUNTRY_PARAM_NAME);
                if (localeLang != null && localeCountry != null) {
                    Locale locale = new Locale(localeLang, localeCountry);
                    params = getConfigService().getLocalizedParameters(locale);
                    getSession().setAttribute(LOCALIZED_PARAMS_ATTR_NAME, params);
                }
            }
        }
        String value = (params != null) ? params.get(key) : null;
        if (value == null) {
            value = LocaleUtils.getValue(key);
        }
        logger.info("Localized string: key = " + key + ", value = " + value);
        return (value != null) ? value : "";
    }

}
