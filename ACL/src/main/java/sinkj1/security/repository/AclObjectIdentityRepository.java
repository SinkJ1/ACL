package sinkj1.security.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclClass;
import sinkj1.security.domain.AclObjectIdentity;

import java.util.Optional;

/**
 * Spring Data SQL repository for the AclObjectIdentity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclObjectIdentityRepository extends JpaRepository<AclObjectIdentity, Long> {

    @Query(
        value = "Select * from acl_object_identity where object_id_identity = ?1 and object_id_class = ?2",
        nativeQuery = true
    )
    Optional<AclObjectIdentity> getByClassIdAndIdIdentityFields(String objectIdIdentity, int aclClass);

}
