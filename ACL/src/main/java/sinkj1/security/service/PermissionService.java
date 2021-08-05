package sinkj1.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Transactional
public class PermissionService {
    @Autowired
    private MutableAclService aclService;
    @Autowired
    private PlatformTransactionManager transactionManager;

    public void addPermissionForUser(Long id, String className, Permission permission, String username) {
        final Sid sid = new PrincipalSid(username);
        addPermissionForSid(id, className, permission, sid);
    }
    public void addPermissionForAuthority(Long id, String className, Permission permission, String authority) {
        final Sid sid = new GrantedAuthoritySid(authority);
        addPermissionForSid(id, className, permission, sid);
    }
    private void addPermissionForSid(Long id, String className, Permission permission, Sid sid) {
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final ObjectIdentity oi = new ObjectIdentityImpl(className, id);
                MutableAcl acl;
                try {
                    acl = (MutableAcl) aclService.readAclById(oi);
                } catch (final NotFoundException nfe) {
                    acl = aclService.createAcl(oi);
                }
                acl.insertAce(acl.getEntries()
                    .size(), permission, sid, true);
                aclService.updateAcl(acl);
            }
        });
    }
}
