package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.util.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * The base abstract form. It is supposed to be extended by KSA subclasses.
 *
 */
public abstract class AbstractForm extends UifFormBase {

    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Returns the locale-aware string value using LocaleUtils for the current locale
     * @param key parameter name
     * @return parameter value
     */
    public String getLValue(String key) {
        String value = LocaleUtils.getValue(key);
        logger.info("Localized string: key = " + key + ", value = " + value);
        return (value != null) ? value : "";
    }

}
