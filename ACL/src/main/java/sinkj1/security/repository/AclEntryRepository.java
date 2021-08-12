package sinkj1.security.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclEntry;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the AclEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclEntryRepository extends JpaRepository<AclEntry, Long> {


    @Query(
        value = "Select  aclob.object_id_identity , mask from acl_entry aclE inner join acl_sid aclS on aclS.id = aclE.sid    inner join acl_object_identity aclob on aclob.id = aclE.acl_object_identity   inner join acl_class ack on ack.id = aclob.object_id_class where aclS.sid = ?1 and ack.class = ?2",
        nativeQuery = true
    )
    List<Object> findByMaskAndAclObjectIdentityNative(String sid, String objectIdIdentity);

    @Query(
        value = "Select * from acl_entry where acl_object_identity = ?1 and ace_order = ?2",
        nativeQuery = true
    )
    Optional<AclEntry> findByObjectIdentityAndAceOrder(int objectIdentity, int aceOrder);

    @Query(
        value = "Select * from acl_entry where acl_object_identity = ?1 and sid = ?2 and mask = ?3",
        nativeQuery = true
    )
    Optional<AclEntry> findByObjectIdentitySidAndMask(int objectIdentity, int sid, int mask);

    @Query(
        value = "select * \n" +
            "from acl_entry ae\n" +
            "inner join acl_object_identity aoi ON aoi.id = ae.acl_object_identity\n" +
            "inner join acl_class ON acl_class.id = aoi.object_id_class\n" +
            "inner join acl_sid ON acl_sid.id = ae.sid\n" +
            "where ae.mask = ?1 and aoi.object_id_identity like ?2 and acl_class.class like ?3 ",
        nativeQuery = true
    )
    Optional<AclEntry> findEntryForUser(int mask, String objectIdentity, String className, String userAuthority);

}


/*
* and acl_sid.sid like ?4
* */
