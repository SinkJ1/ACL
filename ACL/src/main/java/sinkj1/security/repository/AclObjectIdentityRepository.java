package sinkj1.security.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclClass;
import sinkj1.security.domain.AclObjectIdentity;

/**
 * Spring Data SQL repository for the AclObjectIdentity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclObjectIdentityRepository extends JpaRepository<AclObjectIdentity, Long> {
    @Query(value = "Select * from acl_object_identity where object_id_identity = ?1 and object_id_class = ?2", nativeQuery = true)
    Optional<AclObjectIdentity> getByClassIdAndIdIdentityFields(int objectIdIdentity, int aclClass);

    @Query(value = "Select * from acl_object_identity where object_id_identity in ?1 and object_id_class in ?2", nativeQuery = true)
    List<AclObjectIdentity> getByClassIdAndIdIdentityFieldsList(List<Long> objectIdIdentity, List<Long> aclClass);
}
