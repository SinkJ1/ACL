package sinkj1.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sinkj1.security.domain.Tenant;
import sinkj1.security.repository.TenantCrudRepository;
import sinkj1.security.service.TenantService;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantCrudRepository tenantRepository;

    @Autowired
    public TenantServiceImpl(TenantCrudRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Tenant findByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new RuntimeException("No such tenant: " + tenantId));
    }

}
