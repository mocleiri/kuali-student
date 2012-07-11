package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccessControlService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.criteria.*;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.role.RolePermissionBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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

    private static final String CRITERIA_LOOKUP_SERVICE_NAME = "criteriaLookupService";


    private final Set<String> transactionTypeIds = new HashSet<String>();
    private final Map<String, String> transactionTypeMasks = new HashMap<String, String>();

    private IdentityService identityService;
    private RoleService roleService;


    @PostConstruct
    private void postConstruct() {
        identityService = KimApiServiceLocator.getIdentityService();
        roleService = KimApiServiceLocator.getRoleService();
        refresh();
    }

    private void loadTransactionTypeMasks() {
        transactionTypeMasks.clear();
        List<TransactionMaskRole> maskRoles = getEntities(TransactionMaskRole.class);
        for (TransactionMaskRole maskRole : maskRoles) {
            String roleName = maskRole.getRoleName();
            String typeMask = maskRole.getTypeMask();
            logger.debug("Loading [roleName = '" + roleName + "', typeMask = '" + typeMask + "']");
            transactionTypeMasks.put(roleName, typeMask);
        }
    }

    private void loadTransactionTypeIds() {
        transactionTypeIds.clear();
        List<TransactionType> transactionTypes = getEntities(TransactionType.class);
        for (TransactionType transactionType : transactionTypes) {
            String transactionTypeId = transactionType.getId().getId();
            logger.debug("Loading [Transaction Type ID = '" + transactionTypeId + "']");
            transactionTypeIds.add(transactionTypeId);
        }
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
            CriteriaLookupService lookupService = GlobalResourceLoader.getService(CRITERIA_LOOKUP_SERVICE_NAME);
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

    private List<String> getTransactionTypeMasksByRoleNames(Set<String> roleNames) {
        if (roleNames != null && !roleNames.isEmpty()) {
            if (!transactionTypeMasks.isEmpty()) {
                List<String> typeMasks = new LinkedList<String>();
                for (Map.Entry<String, String> roleMask : transactionTypeMasks.entrySet()) {
                    String roleName = roleMask.getKey();
                    String typeMask = roleMask.getValue();
                    if (roleNames.contains(roleName)) {
                        typeMasks.add(typeMask);
                    }
                }
                return typeMasks;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllowedTransactionTypeMasks(String userId) {
        return getTransactionTypeMasksByRoleNames(getRoles(userId));
    }

    @Override
    public List<String> getAllowedTransactionTypes(String userId) {
        return getTransactionTypesByRoleNames(getRoles(userId));
    }

    @Override
    public List<String> getTransactionTypesByRoleNames(Set<String> roleNames) {
        List<String> typeMasks = getTransactionTypeMasksByRoleNames(roleNames);
        List<String> transactionTypes = new LinkedList<String>();
        for (String typeMask : typeMasks) {
            for (String transactionTypeId : transactionTypeIds) {
                if (Pattern.matches(typeMask, transactionTypeId)) {
                    transactionTypes.add(transactionTypeId);
                }
            }
        }
        return transactionTypes;
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
        loadTransactionTypeMasks();
        loadTransactionTypeIds();
    }
}
