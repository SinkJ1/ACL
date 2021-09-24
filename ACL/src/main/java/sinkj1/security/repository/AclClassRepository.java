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

    @Query(
        value = "select ac.class\n" +
        "from acl_entry ae\n" +
        "inner join acl_sid acs on ae.sid = acs.id\n" +
        "inner join acl_object_identity aoi on ae.acl_object_identity = aoi.id\n" +
        "inner join acl_class ac on ac.id = aoi.object_id_class\n" +
        "where acs.sid in ?1 and ae.mask = 1 and aoi.object_id_identity = 0\n" +
        "group by ac.class ",
        nativeQuery = true
    )
    List<String> getAclClassViewForUser(List<String> userAuthorities);
}
