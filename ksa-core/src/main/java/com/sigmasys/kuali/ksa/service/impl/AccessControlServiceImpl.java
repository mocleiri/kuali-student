package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccessControlService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.service.UserSessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Set<String> getRoleNames(String userId) {
        Principal principal = identityService.getPrincipalByPrincipalName(userId);
        Predicate predicate =
                PredicateFactory.equal(KimConstants.PrimaryKeyConstants.PRINCIPAL_ID, principal.getPrincipalId());
        QueryByCriteria criteria = QueryByCriteria.Builder.fromPredicates(predicate);
        List<Role> roles = roleService.findRoles(criteria).getResults();
        if (roles != null && !roles.isEmpty()) {
            Set<String> roleNames = new HashSet<String>(roles.size());
            for (Role role : roles) {
                roleNames.add(role.getName());
            }
            return roleNames;
        }
        return Collections.emptySet();
    }


    @Override
    public List<String> getAllowedTransactionTypeMasks(String userId) {
        Set<String> roleNames = getRoleNames(userId);
        if (!roleNames.isEmpty()) {
            Query query = em.createQuery("select t from " + TransactionMaskRole.class.getSimpleName() + " t " +
                    " where t.roleName in (:roleNames)");
            query.setParameter("roleNames", roleNames);
            List<TransactionMaskRole> maskRoles = query.getResultList();
            if (maskRoles != null && !maskRoles.isEmpty()) {
                List<String> typeMasks = new ArrayList<String>(maskRoles.size());
                for (TransactionMaskRole maskRole : maskRoles) {
                    typeMasks.add(maskRole.getTypeMask());
                }
                return typeMasks;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isTransactionTypeAllowed(String userId, String transactionTypeId) {
        List<String> typeMasks = getAllowedTransactionTypeMasks(userId);
        for ( String typeMask : typeMasks) {
             if ( Pattern.matches(typeMask, transactionTypeId) ) {
                 return true;
             }
        }
        return false;
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
