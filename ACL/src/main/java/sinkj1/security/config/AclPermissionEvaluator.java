package sinkj1.security.config;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityGenerator;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import sinkj1.security.domain.AclEntry;
import sinkj1.security.domain.CustomObjectIdentity;
import sinkj1.security.service.AclEntryService;
import sinkj1.security.service.dto.CustomObjectIdentityImpl;

@Configuration
public class AclPermissionEvaluator implements PermissionEvaluator {

    private final Log logger = LogFactory.getLog(getClass());

    private final AclEntryService aclEntryService;

    private ObjectIdentityGenerator objectIdentityGenerator = new ObjectIdentityRetrievalStrategyImpl();

    private PermissionFactory permissionFactory = new DefaultPermissionFactory();

    public AclPermissionEvaluator(AclEntryService aclEntryService) {
        this.aclEntryService = aclEntryService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {
        if (domainObject == null) {
            return false;
        }

        if (domainObject instanceof Optional) {
            domainObject = ((Optional<?>) domainObject).get();
        }
        ObjectIdentity objectIdentity = null;
        try {
            objectIdentity = new CustomObjectIdentity((String) domainObject.getClass().getDeclaredFields()[0].get(domainObject), (Integer) domainObject.getClass().getDeclaredFields()[1].get(domainObject));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return checkPermission(authentication, new CustomObjectIdentityImpl(objectIdentity.getType(), objectIdentity.getIdentifier()), permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
                                 Object permission) {
        ObjectIdentity objectIdentity = this.objectIdentityGenerator.createObjectIdentity(targetId, targetType);

        return checkPermission(authentication, objectIdentity, permission);
    }

    private boolean checkPermission(Authentication authentication, ObjectIdentity oid, Object permission) {
        // Obtain the SIDs applicable to the principal
        Permission resolvePermission = resolvePermission(permission);
        this.logger.debug(LogMessage.of(() -> "Checking permission '" + permission + "' for object '" + oid + "'"));
        try {
            List<GrantedAuthority> authorities = authentication.getAuthorities().stream().collect(Collectors.toList());
            List<String> authoritiesStrings = authorities.stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
            Optional<AclEntry> aclEntry = aclEntryService.findEntryForUser(resolvePermission.getMask(), String.valueOf(oid.getIdentifier()), oid.getType(), authorities.get(0).toString());

            if (aclEntry.isPresent() && (aclEntry.get().getAclSid().getSid().equals(authentication.getName()) || authoritiesStrings.contains(aclEntry.get().getAclSid().getSid()))) {
                this.logger.debug("Access is granted");
                return true;
            }
            this.logger.debug("Returning false - ACLs returned, but insufficient permissions for this principal");
        } catch (NotFoundException nfe) {
            this.logger.debug("Returning false - no ACLs apply for this principal");
        }
        return false;
    }

    Permission resolvePermission(Object permission) {
        if (permission instanceof Integer) {
            return this.permissionFactory.buildFromMask((Integer) permission);
        }
        if (permission instanceof Permission) {
            return (Permission) permission;
        }
        if (permission instanceof String) {
            String permString = (String) permission;
            Permission p = buildPermission(permString);
            if (p != null) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unsupported permission: " + permission);
    }

    private Permission buildPermission(String permString) {
        try {
            return this.permissionFactory.buildFromName(permString);
        } catch (IllegalArgumentException notfound) {
            return this.permissionFactory.buildFromName(permString.toUpperCase(Locale.ENGLISH));
        }
    }

}
