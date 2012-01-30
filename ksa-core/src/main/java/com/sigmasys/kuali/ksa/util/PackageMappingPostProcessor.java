package com.sigmasys.kuali.ksa.util;

import com.sigmasys.kuali.ksa.annotation.AnnotationUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

import javax.persistence.Entity;
import java.util.Set;

/**
 * PackageMappingPostProcessor is used to narrow the JPA scan scope.
 * User: ivanovm
 * Date: Apr 16, 2010
 * Time: 11:36:34 AM
 */
public class PackageMappingPostProcessor implements PersistenceUnitPostProcessor {

    private String[] packageNames;

    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        Set<String> annotatedEntities = AnnotationUtils.findAnnotatedClasses(Entity.class,
                new DefaultResourceLoader(), packageNames);
        for (String entityClass : annotatedEntities) {
            pui.addManagedClassName(entityClass);
        }
        pui.setExcludeUnlistedClasses(true);
    }

    public void setPackageNames(String[] packageNames) {
        this.packageNames = packageNames;
    }
}