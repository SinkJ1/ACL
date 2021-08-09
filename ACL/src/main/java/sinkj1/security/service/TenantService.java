package sinkj1.security.service;

import org.springframework.data.repository.query.Param;
import sinkj1.security.domain.Tenant;

public interface TenantService {

    Tenant findByTenantId(@Param("tenantId") String tenantId);
}
