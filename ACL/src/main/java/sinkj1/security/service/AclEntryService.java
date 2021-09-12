package sinkj1.security.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sinkj1.security.domain.AclEntry;
import sinkj1.security.domain.MaskAndObject;
import sinkj1.security.service.dto.AclEntryDTO;

/**
 * Service Interface for managing {@link sinkj1.security.domain.AclEntry}.
 */
public interface AclEntryService {
    /**
     * Save a aclEntry.
     *
     * @param aclEntryDTO the entity to save.
     * @return the persisted entity.
     */
    AclEntryDTO save(AclEntryDTO aclEntryDTO);

    /**
     * Partially updates a aclEntry.
     *
     * @param aclEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AclEntryDTO> partialUpdate(AclEntryDTO aclEntryDTO);

    /**
     * Get all the aclEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AclEntryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" aclEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AclEntryDTO> findOne(Long id);

    /**
     * Delete the "id" aclEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<MaskAndObject> getMaskAndObjectId(String objectIdIdentity);

    List<MaskAndObject> getMaskAndObjectIdByUser(String objectIdIdentity, String userName);

    Optional<AclEntry> findEntryForUser(List<Integer> mask, int objectIdentity, String className, List<String> userAuthority);
}
