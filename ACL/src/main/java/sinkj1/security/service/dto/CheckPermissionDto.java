package sinkj1.library.service.dto;

import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.core.Authentication;

public class CheckPermissionDto {

    private Authentication authentication;
    private ObjectIdentity objectIdentity;
    private Object permission;

    public CheckPermissionDto(Authentication authentication, ObjectIdentity objectIdentity, Object permission) {
        this.authentication = authentication;
        this.objectIdentity = objectIdentity;
        this.permission = permission;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public ObjectIdentity getObjectIdentity() {
        return objectIdentity;
    }

    public Object getPermission() {
        return permission;
    }
}
