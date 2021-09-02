package sinkj1.security.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclClass;

/**
 * Spring Data SQL repository for the AclClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclClassRepository extends JpaRepository<AclClass, Long> {
    @Query(value = "select * from acl_class where class like ?1 ", nativeQuery = true)
    Optional<AclClass> findByName(String name);

    @Query(value = "select * from acl_class where class in ?1 ", nativeQuery = true)
    Set<AclClass> findByNames(Set<String> name);

    @Query(value = "select acl_class.id from acl_class where class in ?1 ", nativeQuery = true)
    List<Long> getIdsByNames(List<String> name);
}
