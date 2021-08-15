package sinkj1.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import sinkj1.security.domain.AclSid;

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

    public void addPermissionForAuthority(Long id, String className, Permission permission, String authority) {
        AclSid aclSid = new AclSid(authority);
        addPermissionForSid(id, className, permission, aclSid);
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
}
