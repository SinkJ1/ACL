package sinkj1.security.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclClass;

import java.util.Optional;

/**
 * Spring Data SQL repository for the AclClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclClassRepository extends JpaRepository<AclClass, Long> {

    @Query(value = "select * from acl_class where class like ?1 ", nativeQuery = true)
    Optional<AclClass> findByName(String name);
}
