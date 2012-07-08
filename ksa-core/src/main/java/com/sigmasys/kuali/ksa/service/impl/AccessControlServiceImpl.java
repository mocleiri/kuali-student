package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccessControlService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.service.UserSessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Currency service JPA implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("accessControlService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class AccessControlServiceImpl extends GenericPersistenceService implements AccessControlService {

    private static final Log logger = LogFactory.getLog(AccessControlServiceImpl.class);


    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;


    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserSessionManager userSessionManager;

    private IdentityService identityService;
    private PermissionService permissionService;
    private RoleService roleService;

    @PostConstruct
    private void postConstruct() {
        identityService = KimApiServiceLocator.getIdentityService();
        permissionService = KimApiServiceLocator.getPermissionService();
        roleService = KimApiServiceLocator.getRoleService();
    }


    @Override
    public List<String> getAllowedTransactionTypes(String userId) {
        // TODO
        //roleService.
        //Query query = em.createQuery();
        return null;
    }

    @Override
    public boolean isTransactionTypeAllowed(String userId, TransactionTypeId transactionTypeId) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasPermissions(String userId, String... permissionNames) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasPermission(String userId, String permissionName) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
