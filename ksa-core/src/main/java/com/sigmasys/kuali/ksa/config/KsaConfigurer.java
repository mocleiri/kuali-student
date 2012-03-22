package com.sigmasys.kuali.ksa.config;

import com.sigmasys.kuali.ksa.model.Constants;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.framework.config.module.ModuleConfigurer;
import org.kuali.rice.core.framework.config.module.WebModuleConfiguration;
import org.kuali.rice.core.framework.persistence.ojb.RiceDataSourceConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;
import java.util.List;

/**
 * KSA Module Configurer
 *
 * @author Michael Ivanov
 *         Date: 3/22/12
 *         Time: 12:17 AM
 */
public class KsaConfigurer extends ModuleConfigurer implements ApplicationContextAware {

    private static final String KSA_BEANS_PATH = "classpath:/META-INF/";
    private static final String KSA_BEANS_FILE = KSA_BEANS_PATH + "ksa-beans.xml";

    public KsaConfigurer() {
        super(Constants.MODULE_NAME);
        setValidRunModes(Arrays.asList(RunMode.LOCAL));
    }

    @Override
    protected String getDefaultConfigPackagePath() {
        return KSA_BEANS_PATH;
    }

    @Override
    protected String getDefaultSpringBeansPath(String configPackagePath) {
        return KSA_BEANS_FILE;
    }

    @Override
    public List<String> getPrimarySpringFiles() {
        return Arrays.asList(KSA_BEANS_FILE);
    }

    @Override
    public boolean hasWebInterface() {
        return false;
    }

    @Override
    protected WebModuleConfiguration loadWebModule() {
        WebModuleConfiguration configuration = super.loadWebModule();
        configuration.setWebSpringFiles(Arrays.asList(KSA_BEANS_FILE));
        return configuration;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        // Required by Rice OJB
        RiceDataSourceConnectionFactory.addBeanFactory(applicationContext);
    }
}
