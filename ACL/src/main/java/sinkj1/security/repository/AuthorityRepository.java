package sinkj1.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sinkj1.security.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
