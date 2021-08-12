package sinkj1.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.AclMask;

@Repository
public interface AclMaskRepository extends JpaRepository<AclMask, Long> {



}
