package sinkj1.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sinkj1.security.domain.Tenant;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantCrudRepository extends JpaRepository<Tenant, String> {

    @Query("select t from Tenant t where t.tenantId = :tenantId")
    Optional<Tenant> findByTenantId(@Param("tenantId") String tenantId);

}
