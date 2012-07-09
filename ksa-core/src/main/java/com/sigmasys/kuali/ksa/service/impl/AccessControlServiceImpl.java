package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccessControlService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.service.UserSessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.criteria.*;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.role.RolePermissionBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Access Control service implementation.
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

    private List<Role> getKimRoles(String userId) {
        Principal principal = identityService.getPrincipalByPrincipalName(userId);
        Predicate memberPredicate =
                PredicateFactory.equal(KIMPropertyConstants.RoleMember.MEMBER_ID, principal.getPrincipalId());
        Predicate memberTypePredicate =
                PredicateFactory.equal(KIMPropertyConstants.RoleMember.MEMBER_TYPE_CODE, MemberType.PRINCIPAL.getCode());
        QueryByCriteria criteria = QueryByCriteria.Builder.fromPredicates(memberPredicate, memberTypePredicate);
        List<RoleMember> roleMembers = roleService.findRoleMembers(criteria).getResults();
        if (roleMembers != null && !roleMembers.isEmpty()) {
            List<String> roleIds = new ArrayList<String>(roleMembers.size());
            for (RoleMember roleMember : roleMembers) {
                roleIds.add(roleMember.getRoleId());
            }
            return roleService.getRoles(roleIds);
        }
        return Collections.emptyList();
    }

    @Override
    public Set<String> getRoles(String userId) {
        List<Role> roles = getKimRoles(userId);
        if (!roles.isEmpty()) {
            Set<String> roleNames = new HashSet<String>(roles.size());
            for (Role role : roles) {
                roleNames.add(role.getName());
            }
            return roleNames;
        }
        return Collections.emptySet();
    }

    @Override
    public Set<String> getPermissions(String userId) {
        List<Role> roles = getKimRoles(userId);
        if (!roles.isEmpty()) {
            List<String> roleIds = new ArrayList<String>(roles.size());
            for (Role role : getKimRoles(userId)) {
                roleIds.add(role.getId());
            }
            Predicate predicate = PredicateFactory.in(KIMPropertyConstants.RoleMember.ROLE_ID, roleIds.toArray());
            QueryByCriteria criteria = QueryByCriteria.Builder.fromPredicates(predicate);
            CriteriaLookupService lookupService = GlobalResourceLoader.getService("criteriaLookupService");
            GenericQueryResults<RolePermissionBo> results = lookupService.lookup(RolePermissionBo.class, criteria);
            List<RolePermissionBo> permissions = results.getResults();
            if (permissions != null && !permissions.isEmpty()) {
                Set<String> permissionNames = new HashSet<String>(permissions.size());
                for (RolePermissionBo permission : permissions) {
                    permissionNames.add(permission.getPermission().getName());
                }
                return permissionNames;
            }
        }
        return Collections.emptySet();
    }


    @Override
    public List<String> getAllowedTransactionTypeMasks(String userId) {
        Set<String> roleNames = getRoles(userId);
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
        for (String typeMask : typeMasks) {
            if (Pattern.matches(typeMask, transactionTypeId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasPermissions(String userId, String... permissionNames) {
        Set<String> permissions = getPermissions(userId);
        for (String permissionName : permissionNames) {
            if (!permissions.contains(permissionName)) {
                return false;
            }
        }
        return !permissions.isEmpty() && permissionNames.length > 0;
    }

    @Override
    public boolean hasPermission(String userId, String permissionName) {
        return hasPermissions(userId, permissionName);
    }

    @Override
    public void refresh() {
        // TODO: Reload the attributes from the KIM and KSA databases
    }
}
