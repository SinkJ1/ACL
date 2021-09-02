package sinkj1.security.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclEntry;

/**
 * Spring Data SQL repository for the AclEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclEntryRepository extends JpaRepository<AclEntry, Long> {
    @Query(
        value = "Select * from acl_entry aclE inner join acl_sid aclS on aclS.id = aclE.sid    inner join acl_object_identity aclob on aclob.id = aclE.acl_object_identity   inner join acl_class ack on ack.id = aclob.object_id_class where ack.class like ?1 and (aclS.sid = ?2 or aclS.sid in ?3)",
        nativeQuery = true
    )
    List<AclEntry> findByMaskAndAclObjectIdentityNative(String objectIdIdentity, String name, List<String> authoritiesStrings);

    @Query(value = "Select * from acl_entry where acl_object_identity = ?1 and ace_order = ?2", nativeQuery = true)
    Optional<AclEntry> findByObjectIdentityAndAceOrder(int objectIdentity, int aceOrder);

    @Query(value = "Select * from acl_entry where acl_object_identity = ?1 and sid = ?2 and mask = ?3", nativeQuery = true)
    Optional<AclEntry> findByObjectIdentitySidAndMask(int objectIdentity, int sid, int mask);

    @Query(value = "Select * from acl_entry where acl_object_identity in ?1 and sid in ?2 and mask in ?3", nativeQuery = true)
    List<AclEntry> findByObjectIdentitySidAndMaskList(List<Integer> objectIdentity, List<Integer> sid, List<Integer> mask);

    @Query(
        value = "select * \n" +
        "from acl_entry ae\n" +
        "inner join acl_object_identity aoi ON aoi.id = ae.acl_object_identity\n" +
        "inner join acl_class ON acl_class.id = aoi.object_id_class\n" +
        "inner join acl_sid ON acl_sid.id = ae.sid\n" +
        "where ae.mask in ?1 and aoi.object_id_identity = ?2 and acl_class.class like ?3 and acl_sid.sid in ?4",
        nativeQuery = true
    )
    Optional<AclEntry> findEntryForUser(List<Integer> mask, int objectIdentity, String className, List<String> userAuthority);
}
