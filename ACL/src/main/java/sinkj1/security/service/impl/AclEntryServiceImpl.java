package sinkj1.security.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.security.domain.AclEntry;
import sinkj1.security.repository.AclEntryRepository;
import sinkj1.security.service.AclEntryService;
import sinkj1.security.service.dto.AclEntryDTO;
import sinkj1.security.service.mapper.AclEntryMapper;

/**
 * Service Implementation for managing {@link AclEntry}.
 */
@Service
@Transactional
public class AclEntryServiceImpl implements AclEntryService {

    private final Logger log = LoggerFactory.getLogger(AclEntryServiceImpl.class);

    private final AclEntryRepository aclEntryRepository;

    private final AclEntryMapper aclEntryMapper;

    public AclEntryServiceImpl(AclEntryRepository aclEntryRepository, AclEntryMapper aclEntryMapper) {
        this.aclEntryRepository = aclEntryRepository;
        this.aclEntryMapper = aclEntryMapper;
    }

    @Override
    public AclEntryDTO save(AclEntryDTO aclEntryDTO) {
        log.debug("Request to save AclEntry : {}", aclEntryDTO);
        AclEntry aclEntry = aclEntryMapper.toEntity(aclEntryDTO);
        aclEntry = aclEntryRepository.save(aclEntry);
        return aclEntryMapper.toDto(aclEntry);
    }

    @Override
    public Optional<AclEntryDTO> partialUpdate(AclEntryDTO aclEntryDTO) {
        log.debug("Request to partially update AclEntry : {}", aclEntryDTO);

        return aclEntryRepository
            .findById(aclEntryDTO.getId())
            .map(
                existingAclEntry -> {
                    aclEntryMapper.partialUpdate(existingAclEntry, aclEntryDTO);

                    return existingAclEntry;
                }
            )
            .map(aclEntryRepository::save)
            .map(aclEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AclEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AclEntries");
        return aclEntryRepository.findAll(pageable).map(aclEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AclEntryDTO> findOne(Long id) {
        log.debug("Request to get AclEntry : {}", id);
        return aclEntryRepository.findById(id).map(aclEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AclEntry : {}", id);
        aclEntryRepository.deleteById(id);
    }

    @Override
    public List<Object> getMaskAndObjectId(String sid, String objectIdIdentity) {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return aclEntryRepository.findByMaskAndAclObjectIdentityNative(sid, objectIdIdentity);
    }

    @Override
    public Optional<AclEntry> findEntryForUser(int mask, String objectIdentity, String className, String userAuthority) {
        return aclEntryRepository.findEntryForUser(mask, objectIdentity, className, userAuthority);
    }

}
