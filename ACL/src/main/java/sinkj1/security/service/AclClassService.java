package sinkj1.security.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sinkj1.security.service.dto.AclClassDTO;

/**
 * Service Interface for managing {@link sinkj1.security.domain.AclClass}.
 */
public interface AclClassService {
    /**
     * Save a aclClass.
     *
     * @param aclClassDTO the entity to save.
     * @return the persisted entity.
     */
    AclClassDTO save(AclClassDTO aclClassDTO);

    /**
     * Partially updates a aclClass.
     *
     * @param aclClassDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AclClassDTO> partialUpdate(AclClassDTO aclClassDTO);

    /**
     * Get all the aclClasses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AclClassDTO> findAll(Pageable pageable);

    /**
     * Get the "id" aclClass.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AclClassDTO> findOne(Long id);

    /**
     * Delete the "id" aclClass.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
