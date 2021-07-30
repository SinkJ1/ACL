package sinkj1.security.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.security.domain.AclObjectIdentity;
import sinkj1.security.repository.AclObjectIdentityRepository;
import sinkj1.security.service.AclObjectIdentityService;
import sinkj1.security.service.dto.AclObjectIdentityDTO;
import sinkj1.security.service.mapper.AclObjectIdentityMapper;

/**
 * Service Implementation for managing {@link AclObjectIdentity}.
 */
@Service
@Transactional
public class AclObjectIdentityServiceImpl implements AclObjectIdentityService {

    private final Logger log = LoggerFactory.getLogger(AclObjectIdentityServiceImpl.class);

    private final AclObjectIdentityRepository aclObjectIdentityRepository;

    private final AclObjectIdentityMapper aclObjectIdentityMapper;

    public AclObjectIdentityServiceImpl(
        AclObjectIdentityRepository aclObjectIdentityRepository,
        AclObjectIdentityMapper aclObjectIdentityMapper
    ) {
        this.aclObjectIdentityRepository = aclObjectIdentityRepository;
        this.aclObjectIdentityMapper = aclObjectIdentityMapper;
    }

    @Override
    public AclObjectIdentityDTO save(AclObjectIdentityDTO aclObjectIdentityDTO) {
        log.debug("Request to save AclObjectIdentity : {}", aclObjectIdentityDTO);
        AclObjectIdentity aclObjectIdentity = aclObjectIdentityMapper.toEntity(aclObjectIdentityDTO);
        aclObjectIdentity = aclObjectIdentityRepository.save(aclObjectIdentity);
        return aclObjectIdentityMapper.toDto(aclObjectIdentity);
    }

    @Override
    public Optional<AclObjectIdentityDTO> partialUpdate(AclObjectIdentityDTO aclObjectIdentityDTO) {
        log.debug("Request to partially update AclObjectIdentity : {}", aclObjectIdentityDTO);

        return aclObjectIdentityRepository
            .findById(aclObjectIdentityDTO.getId())
            .map(
                existingAclObjectIdentity -> {
                    aclObjectIdentityMapper.partialUpdate(existingAclObjectIdentity, aclObjectIdentityDTO);

                    return existingAclObjectIdentity;
                }
            )
            .map(aclObjectIdentityRepository::save)
            .map(aclObjectIdentityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AclObjectIdentityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AclObjectIdentities");
        return aclObjectIdentityRepository.findAll(pageable).map(aclObjectIdentityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AclObjectIdentityDTO> findOne(Long id) {
        log.debug("Request to get AclObjectIdentity : {}", id);
        return aclObjectIdentityRepository.findById(id).map(aclObjectIdentityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AclObjectIdentity : {}", id);
        aclObjectIdentityRepository.deleteById(id);
    }
}
