package sinkj1.security.multi_tenancy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sinkj1.security.multi_tenancy.domain.entity.Tenant;
import sinkj1.security.multi_tenancy.repository.TenantRepository;

//@Service
public class MultiTenantServiceImpl implements TenantService {

   /* private final TenantRepository tenantRepository;

    @Autowired
    public MultiTenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Tenant findByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new RuntimeException("No such tenant: " + tenantId));
    }*/

}
