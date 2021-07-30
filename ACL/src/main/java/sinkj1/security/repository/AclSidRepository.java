package sinkj1.security.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclSid;

/**
 * Spring Data SQL repository for the AclSid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclSidRepository extends JpaRepository<AclSid, Long> {}
