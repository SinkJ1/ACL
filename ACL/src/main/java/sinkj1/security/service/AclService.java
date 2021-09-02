package sinkj1.security.service;

import java.util.List;
import org.springframework.security.acls.model.Permission;
import sinkj1.security.domain.AclSid;
import sinkj1.security.service.dto.DeletePermissionDto;
import sinkj1.security.service.dto.PermissionDto;

public interface AclService {
    void createPermission(Long id, String className, Permission permission, AclSid sid);

    void createPermissions(List<PermissionDto> permissionDtos);

    void deletePermission(DeletePermissionDto deletePermissionDto);
}
