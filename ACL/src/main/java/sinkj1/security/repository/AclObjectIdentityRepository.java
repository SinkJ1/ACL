package sinkj1.security.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclObjectIdentity;

/**
 * Spring Data SQL repository for the AclObjectIdentity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclObjectIdentityRepository extends JpaRepository<AclObjectIdentity, Long> {}
