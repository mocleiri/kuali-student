package com.sigmasys.kuali.ksa.config;

import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.impl.config.property.ConfigFactoryBean;
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl;
import org.springframework.transaction.annotation.Transactional;


/**
 * RiceConfigFactoryBean.
 *
 * @author Michael Ivanov
 */
@Transactional
public class RiceConfigFactoryBean extends ConfigFactoryBean {

    public static final String APPLICATION_HOST_PARAM_NAME = "application.host";

    private boolean initialize;

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

        // Additional properties set up
        config.putProperty(APPLICATION_HOST_PARAM_NAME, RequestUtils.getIPAddress());

        config.parseConfig();

        if (initialize) {
            ConfigContext.init(config);
        }

        return config;

    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

}
