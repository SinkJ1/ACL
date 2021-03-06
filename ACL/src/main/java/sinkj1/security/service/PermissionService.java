package sinkj1.security.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import sinkj1.security.domain.AclSid;
import sinkj1.security.service.dto.DeletePermissionDto;
import sinkj1.security.service.dto.PermissionDto;

@Service
@Transactional
public class PermissionService {

    @Autowired
    private final AclService aclService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public PermissionService(AclService aclService) {
        this.aclService = aclService;
    }

    public void addPermissionForUser(Long id, String className, Permission permission, String username) {
        AclSid aclSid = new AclSid(username);
        addPermissionForSid(id, className, permission, aclSid);
    }

    public void addPermissionsForUser(List<PermissionDto> permissionDtoList) {
        addPermissionForSids(permissionDtoList);
    }

    public void addPermissionForAuthority(Long id, String className, Permission permission, String authority) {
        AclSid aclSid = new AclSid(authority);
        addPermissionForSid(id, className, permission, aclSid);
    }

    public void deletePermission(DeletePermissionDto deletePermissionDto) {
        deletePermissionAtSid(deletePermissionDto);
    }

    private void addPermissionForSid(Long id, String className, Permission permission, AclSid sid) {
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.execute(
            new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    aclService.createPermission(id, className, permission, sid);
                }
            }
        );
    }

    private void addPermissionForSids(List<PermissionDto> permissionDtos) {
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.execute(
            new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    for (PermissionDto permissionDto : permissionDtos) {
                        AclSid aclSid = new AclSid(permissionDto.getSid());
                        aclService.createPermission(
                            permissionDto.getId(),
                            permissionDto.getClassName(),
                            convertFromIntToBasePermission(permissionDto.getPermission()),
                            aclSid
                        );
                    }
                }
            }
        );
    }

    private void deletePermissionAtSid(DeletePermissionDto deletePermissionDto) {
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.execute(
            new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    aclService.deletePermission(deletePermissionDto);
                }
            }
        );
    }

    private Permission convertFromIntToBasePermission(int permission) {
        switch (permission) {
            case 2:
                return BasePermission.WRITE;
            case 4:
                return BasePermission.CREATE;
            case 8:
                return BasePermission.DELETE;
            case 16:
                return BasePermission.ADMINISTRATION;
            default:
                return BasePermission.READ;
        }
    }
}
