package sinkj1.security.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sinkj1.security.service.dto.AclObjectIdentityDTO;

/**
 * Service Interface for managing {@link sinkj1.security.domain.AclObjectIdentity}.
 */
public interface AclObjectIdentityService {
    /**
     * Save a aclObjectIdentity.
     *
     * @param aclObjectIdentityDTO the entity to save.
     * @return the persisted entity.
     */
    AclObjectIdentityDTO save(AclObjectIdentityDTO aclObjectIdentityDTO);

    /**
     * Partially updates a aclObjectIdentity.
     *
     * @param aclObjectIdentityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AclObjectIdentityDTO> partialUpdate(AclObjectIdentityDTO aclObjectIdentityDTO);

    /**
     * Get all the aclObjectIdentities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AclObjectIdentityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" aclObjectIdentity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AclObjectIdentityDTO> findOne(Long id);

    /**
     * Delete the "id" aclObjectIdentity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
