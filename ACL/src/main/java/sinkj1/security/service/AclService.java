package sinkj1.security.service;

import org.springframework.security.acls.model.Permission;
import sinkj1.security.domain.AclSid;

public interface AclService {

    public void createPermission(Long id, String className, Permission permission, AclSid sid);

}
