package sinkj1.security.service;

import java.sql.SQLException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import sinkj1.security.domain.Tenant;
import sinkj1.security.service.dto.TenantDTO;

public interface TenantService {
    TenantDTO createTenant(TenantDTO tenantDTO);

    TenantDTO save(TenantDTO tenantDTO) throws SQLException;

    Tenant findByTenantId(@Param("tenantId") String tenantId);

    Optional<TenantDTO> partialUpdate(TenantDTO tenantDTO);

    /**
     * Get all the tenants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TenantDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tenant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TenantDTO> findOne(Long id);

    /**
     * Delete the "id" tenant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id) throws SQLException;
}
