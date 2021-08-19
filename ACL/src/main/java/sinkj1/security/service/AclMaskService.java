package sinkj1.security.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sinkj1.security.service.dto.AclMaskDTO;

/**
 * Service Interface for managing {@link sinkj1.security.domain.AclMask}.
 */
public interface AclMaskService {
    /**
     * Save a aclMask.
     *
     * @param aclMaskDTO the entity to save.
     * @return the persisted entity.
     */
    AclMaskDTO save(AclMaskDTO aclMaskDTO);

    /**
     * Partially updates a aclMask.
     *
     * @param aclMaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AclMaskDTO> partialUpdate(AclMaskDTO aclMaskDTO);

    /**
     * Get all the aclMasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AclMaskDTO> findAll(Pageable pageable);

    /**
     * Get the "id" aclMask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AclMaskDTO> findOne(Long id);

    /**
     * Delete the "id" aclMask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
