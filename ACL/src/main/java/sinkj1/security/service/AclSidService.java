package sinkj1.security.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sinkj1.security.service.dto.AclSidDTO;

/**
 * Service Interface for managing {@link sinkj1.security.domain.AclSid}.
 */
public interface AclSidService {
    /**
     * Save a aclSid.
     *
     * @param aclSidDTO the entity to save.
     * @return the persisted entity.
     */
    AclSidDTO save(AclSidDTO aclSidDTO);

    /**
     * Partially updates a aclSid.
     *
     * @param aclSidDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AclSidDTO> partialUpdate(AclSidDTO aclSidDTO);

    /**
     * Get all the aclSids.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AclSidDTO> findAll(Pageable pageable);

    /**
     * Get the "id" aclSid.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AclSidDTO> findOne(Long id);

    /**
     * Delete the "id" aclSid.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
