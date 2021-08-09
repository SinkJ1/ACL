package sinkj1.security.multi_tenancy.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import sinkj1.security.multi_tenancy.domain.entity.Tenant;

import java.util.Optional;

@Component
@Qualifier(value="schemaBasedMultiTenantConnectionProvider")
public interface TenantRepository extends JpaRepository<Tenant, String> {
    @Query("select t from Tenant t where t.tenantId = :tenantId")
    Optional<Tenant> findByTenantId(@Param("tenantId") String tenantId);
}
