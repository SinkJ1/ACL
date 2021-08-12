package sinkj1.security.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclClass;
import sinkj1.security.domain.AclSid;

import java.util.Optional;

/**
 * Spring Data SQL repository for the AclSid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclSidRepository extends JpaRepository<AclSid, Long> {

    @Query(value = "select * from acl_sid where acl_sid.sid like ?1 ", nativeQuery = true)
    Optional<AclSid> findByName(String name);

}
