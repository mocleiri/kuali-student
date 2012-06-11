package com.sigmasys.kuali.ksa.config;

import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.impl.config.property.ConfigFactoryBean;
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl;


/**
 * RiceConfigFactoryBean.
 *
 * @author Michael Ivanov
 */
public class RiceConfigFactoryBean extends ConfigFactoryBean {


    @Override
    public Config getObject() throws Exception {

        if (getConfigLocations() == null) {
            throw new ConfigurationException("No config locations declared, at least one is required");
        }

        Config oldConfig = null;
        if (ConfigContext.getCurrentContextConfig() != null) {
            oldConfig = ConfigContext.getCurrentContextConfig();
        }
        JAXBConfigImpl config;
        if (CONFIG_OVERRIDE_LOCATION != null) {
            config = new JAXBConfigImpl(CONFIG_OVERRIDE_LOCATION, oldConfig);
        } else {
            config = new JAXBConfigImpl(getConfigLocations(), oldConfig);
        }

        return config;

    }

}
