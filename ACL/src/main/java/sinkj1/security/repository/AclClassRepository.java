package sinkj1.security.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclClass;

/**
 * Spring Data SQL repository for the AclClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclClassRepository extends JpaRepository<AclClass, Long> {}
